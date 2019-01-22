(ns attendance-app.events
  (:require
   [re-frame.core :refer [reg-event-db after dispatch reg-event-fx]]
   [clojure.spec.alpha :as s]
   [attendance-app.effects]
   [attendance-app.utils :refer [current-day]]
   [attendance-app.db :as db :refer [app-db]]))

; TODO: get from config
(def host "http://10.0.2.2:3000/")

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

; -- Handlers --
(reg-event-fx :list-attendants
              (fn [_ [_ day]] {:dispatch [::api-get :attendants (str "attendances/" day)]}))

(reg-event-db :process-response
              (fn [db [_ db-key response]]
                (if (contains? response :errors)
                  (dispatch [:show-error (:errors response)]))
                (-> db (assoc db-key response) (assoc :loading? false))))

(reg-event-db :set-attendant-first-name
              (fn [db [_ value]] (assoc db :attendant-first-name value)))

(reg-event-db :set-attendant-last-name
              (fn [db [_ value]] (assoc db :attendant-last-name value)))

(reg-event-fx :create-attendant
              (fn [{db :db} [_ navigate]]
                (let [{:keys [attendant-first-name attendant-last-name]} db
                      on-success #(doseq [event [[:process-response :attendant-form %]
                                                 [:list-attendants (current-day "yyyy-MM-dd")]
                                                 [:navigate navigate "AttendantsList"]]]
                                    (dispatch event))
                      attendant-form {:firstName attendant-first-name :lastName attendant-last-name}]
                  {:dispatch [::api-post "attendants" attendant-form on-success]
                   :db       (dissoc db :attendant-first-name :attendant-last-name)})))

(reg-event-fx :show-error (fn [_db [_ msg]] {:alert msg}))

(reg-event-db :initialize-db validate-spec (fn [_ _] app-db))

(reg-event-fx :navigate (fn [_db [_ navigate address]] {:navigate {:nav-func navigate :address address}}))

(reg-event-fx ::api-get
              (fn [{db :db} [_ db-key url]]
                {:fetch
                 {:url        (str host url)
                  :on-success #(dispatch [:process-response db-key %])
                  :on-failure #(dispatch [:show-error %])}
                 :db (assoc db :loading? true)}))

(reg-event-fx ::api-post
              (fn [{db :db} [_ url body on-success]]
                {:fetch {:method     "POST"
                         :url        (str host url)
                         :body       (.stringify js/JSON (clj->js body))
                         :on-success on-success
                         :on-failure #(dispatch [:show-error %])}
                 :db    (assoc db :loading? true)}))
