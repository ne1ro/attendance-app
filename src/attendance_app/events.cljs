(ns attendance-app.events
  (:require
    [re-frame.core :refer [reg-event-db after dispatch]]
    [clojure.spec.alpha :as s]
    [attendance-app.alert :as a]
    [attendance-app.effects]
    [attendance-app.utils :refer [current-day]]
    [attendance-app.db :as db :refer [app-db]]))

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
(defn- show-error [message] (a/alert "Request Error" message))

(reg-event-db :list-attendants
              (fn [_db [_ day]] (dispatch [::api-get (str "attendants/" day)])))

(reg-event-db :process-response
              (fn [db [_ [db-key response]]]
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
                             "attendants"
                             {:firstName first-name :lastName last-name}])
                  {:firstName first-name :lastName last-name})))

(reg-event-db :show-error (fn [_db msg] (prn msg) (show-error msg)))

(reg-event-db :initialize-db validate-spec (fn [_ _] app-db))
