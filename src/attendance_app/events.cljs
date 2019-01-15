(ns attendance-app.events
  (:require
   [re-frame.core :refer [reg-event-db after dispatch]]
   [clojure.spec.alpha :as s]
   [attendance-app.alert :as a]
   [attendance-app.api :as api]
   [attendance-app.db :as db :refer [app-db]]))

(defn- show-error [message] (a/alert "Request Error" message))

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
(reg-event-db :list-attendants
              (fn [db [_ day]]
                (api/list-attendants day #(dispatch [:process-response %]) show-error)
                (assoc db :loading? true)))

(reg-event-db :process-response
              (fn [db [_ response]]
                (-> db (assoc :attendants response) (assoc :loading? false))))

(reg-event-db :set-attendant-first-name
  (fn [db [_ value]] (assoc db :attendant-first-name value)))

(reg-event-db :set-attendant-last-name
  (fn [db [_ value]] (assoc db :attendant-last-name value)))

(reg-event-db :create-attendant
              (fn [db _]
                (let [{first-name :attendant-first-name last-name :attendant-last-name} db]
                  (api/create-attendant
                    {:firstName first-name :lastName last-name}
                    prn show-error)
                  (-> db
                    (assoc :loading? true)
                    (dissoc :attendant-first-name :attendant-last-name)))))

(reg-event-db
 :initialize-db
 validate-spec
 (fn [_ _] app-db))
