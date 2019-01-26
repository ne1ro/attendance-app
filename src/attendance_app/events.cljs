(ns attendance-app.events
  (:require
    [re-frame.core :refer [reg-event-db after dispatch reg-event-fx]]
    [clojure.spec.alpha :as s]
    [attendance-app.effects]
    [attendance-app.utils :refer [current-day]]
    [attendance-app.db :as db :refer [app-db]]))

; TODO: get from config
(def host "http://10.0.2.2:3000/")

(defn- eq-id? [id1 {id2 :id}] (= id1 id2))

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
(reg-event-db :initialize-db validate-spec (fn [_ _] app-db))

(reg-event-fx :check-access-token
              (fn [{db :db} [_ navigate]]
                (when (-> db :token empty? not)
                  (let [day (current-day "yyyy-MM-dd")]
                    {:dispatch [:list-attendants day]
                     :navigate {:nav-func navigate
                                :address  "AttendantsList"
                                :params   {:day day}}}))))

(reg-event-db :process-response
              (fn [db [_ db-key response]]
                (if (contains? response :errors)
                  (dispatch [:show-error (:errors response)]))
                (-> db (assoc db-key response) (assoc :loading? false))))

(reg-event-fx :show-error (fn [_db [_ msg]] {:alert {:message msg :title "Request Error"}}))

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

(reg-event-fx :list-attendants
              (fn [_ [_ day navigate]]
                {:dispatch [::api-get :attendants (str "attendances/" day)]
                 :navigate {:nav-func navigate :address "AttendantsList" :params {:day day}}}))

(reg-event-fx :list-attendances-days
              (fn [_ [_ navigate]]
                {:dispatch [::api-get :attendances-days "attendances_days"]
                 :navigate {:nav-func navigate :address "AttendancesCalendar"}}))

(reg-event-fx :get-attendant
              (fn [_ [_ navigate id]]
                {:dispatch-n [[:navigate navigate "Attendant"] [::api-get :attendant (str "attendants/" id)]]}))

(reg-event-db :set-attendant-first-name
              (fn [db [_ value]] (assoc db :attendant-first-name value)))

(reg-event-db :set-attendant-last-name
              (fn [db [_ value]] (assoc db :attendant-last-name value)))

(reg-event-fx :show-delete-dialogue
              (fn [_ [_ id]]
                {:alert {:title     "Are you sure about removal?"
                         :message   "This will remove all attendances and can't be undone"
                         :on-ok     #(dispatch [:delete-attendant id])
                         :on-cancel #(prn "Canceled")}}))

(reg-event-fx :delete-attendant
              (fn [{db :db} [_ id]]
                {:dispatch [::api-delete (str "attendants/" id) prn]
                 :db       (update db :attendants #(remove (partial eq-id? id) %))}))

(defn- find-index [attendants id]
  (->> attendants (filter (partial eq-id? id)) first (.indexOf attendants)))

(defn- toggle-status [attendant]
  (update attendant :status #(if (= "unattended" %) "attended" "unattended")))

(reg-event-fx :toggle-attendance
              (fn [{db :db} [_ {:keys [id status]} day]]
                (let [url (str "attendants/" id "/attendances")
                      update-status #(update % (find-index % id) toggle-status)]
                  (if (= "unattended" status)
                    {:dispatch [::api-post url {:status true :day day} prn]
                     :db       (update db :attendants update-status)}
                    {:dispatch [::api-delete (str url "/" day) prn]
                     :db       (update db :attendants update-status)}))))

(reg-event-fx :navigate
              (fn [_db [_ navigate address]] {:navigate {:nav-func navigate :address address}}))

(reg-event-fx ::api-get
              (fn [{db :db} [_ db-key url]]
                {:fetch {:url        (str host url)
                         :on-success #(dispatch [:process-response db-key %])
                         :on-failure #(dispatch [:show-error %])}
                 :db    (assoc db :loading? true)}))

(reg-event-fx ::api-post
              (fn [{db :db} [_ url body on-success]]
                {:fetch {:method     "POST"
                         :url        (str host url)
                         :body       (.stringify js/JSON (clj->js body))
                         :on-success on-success
                         :on-failure #(dispatch [:show-error %])}
                 :db    (assoc db :loading? true)}))

(reg-event-fx ::api-delete
              (fn [_ [_ url on-success]]
                {:fetch {:method     "DELETE"
                         :url        (str host url)
                         :on-success on-success
                         :on-failure #(dispatch [:show-error %])}}))
