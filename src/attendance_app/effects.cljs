(ns attendance-app.effects
  (:require
   [re-frame.core :refer [reg-fx]]
   [attendance-app.alert :as a]))

(defn- handle-response [resp success-handler]
  (->
   resp
   .json
   (.then (fn [data] (success-handler (js->clj data :keywordize-keys true))))))

(defn- fetch [{:keys [url on-success on-failure] :as params}]
  (let [default-params {:method "GET" :headers {"Content-Type" "application/json"}}]
    (->
     url
     (js/fetch (clj->js (merge default-params params)))
     (.then #(handle-response % on-success))
     (.catch #(-> % .-message on-failure)))))

(reg-fx :alert (fn [msg] (a/alert "Request Error" msg)))
(reg-fx :fetch fetch)
(reg-fx :navigate (fn [{:keys [nav-func address]}] (nav-func address)))