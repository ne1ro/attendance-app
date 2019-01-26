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

(reg-fx :fetch fetch)
(reg-fx :navigate (fn [{:keys [nav-func address params]}] (nav-func address (clj->js params))))
(reg-fx :alert
        (fn [{:keys [title message on-ok on-cancel]}]
          (if on-ok
            (a/alert title message on-ok on-cancel)
            (a/alert title message))))
