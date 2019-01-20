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
  (let [{url :url on-success :on-success on-failure :on-failure} params]
    (fetch
      url params
      (dispatch on-success)
      (dispatch on-failure))))

(reg-fx :alert [msg] (a/alert "Request Error" msg))
(reg-fx :fetch fetch-effect)
