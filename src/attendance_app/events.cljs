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
                (-> db (assoc db-key response) (assoc :loading? false))))

(reg-event-db :set-attendant-first-name
              (fn [db [_ value]] (assoc db :attendant-first-name value)))

(reg-event-db :set-attendant-last-name
              (fn [db [_ value]] (assoc db :attendant-last-name value)))

(reg-event-db :create-attendant
              (fn [db [_ navigate]]
                ; TODO: use navigation func
                (let [{first-name :attendant-first-name last-name :attendant-last-name} db]
                  (dispatch [::api-post
                             :attendant-form
                             "attendants"
                             {:firstName first-name :lastName last-name}])
                  {:firstName first-name :lastName last-name})))

(reg-event-fx :show-error
              (fn [_db [_ msg]] {:alert msg}))

(reg-event-db :initialize-db validate-spec (fn [_ _] app-db))

(reg-event-fx ::api-get
              (fn [{db :db} [_ db-key url]]
                {:fetch
                     {:url        (str host url)
                      :db-key     db-key
                      :on-success [:process-response]
                      :on-failure [:show-error]}
                 :db (assoc db :loading? true)}))

(reg-event-fx ::api-post
              (fn [{db :db} [_ url db-key body]]
                {:fetch {:method     "POST"
                         :db-key     db-key
                         :url        (str host url)
                         :body       body
                         :on-success [:process-response]
                         :on-failure [:show-error]}
                 :db    (assoc db :loading? true)}))
