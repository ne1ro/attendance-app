(ns attendance-app.effects
  (:require
   [re-frame.core :refer [reg-fx]]
   [attendance-app.alert :as a]))

(def async-storage (-> "react-native" js/require .-AsyncStorage))

(defn- handle-response [resp success-handler]
  (->
   resp
   .json
   (.then (fn [data] (success-handler (js->clj data :keywordize-keys true))))))

(defn- fetch [{:keys [url on-success on-failure token] :as params}]
  (prn token)
  (let [default-params
        {:method  "GET"
         :headers {"Content-Type" "application/json" "X-Auth-Token" token}}]
    (->
     url
     (js/fetch (clj->js (merge default-params params)))
     (.then #(handle-response % on-success))
     (.catch #(do (prn %) (-> % .-message on-failure))))))

(reg-fx :fetch fetch)
(reg-fx :navigate
        (fn [{:keys [nav-func address params]}]
          (if params
            (nav-func address (clj->js params))
            (nav-func address))))

(reg-fx :alert
        (fn [{:keys [title message on-ok on-cancel]}]
          (if on-ok
            (a/alert title message on-ok on-cancel)
            (a/alert title message))))
