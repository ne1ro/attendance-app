(ns attendance-app.effects
  (:require
    [re-frame.core :refer [reg-fx dispatch]]
    [attendance-app.alert :as a]))

(defn- handle-response [resp success-handler]
  (->
    resp
    .json
    (.then (fn [data] (success-handler (js->clj data :keywordize-keys true))))))

(defn- fetch [url params success-handler err-handler]
  (let [default-params {:method "GET" :headers {"Content-Type" "application/json"}}]
    (->
      url
      (js/fetch (clj->js (merge default-params params)))
      (.then #(handle-response % success-handler))
      (.catch #(-> % .-message err-handler)))))

(defn- fetch-effect [params]
  (let [{:keys [url db-key on-success on-failure]} params]
    (fetch
      url
      params
      #(-> on-success (conj db-key %) dispatch)
      #(-> on-failure (conj %) dispatch))))

(reg-fx :alert
        (fn [msg] (a/alert "Request Error" msg)))
(reg-fx :fetch fetch-effect)
