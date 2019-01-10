(ns attendance-app.api
  (:require [re-frame.core :refer [dispatch dispatch-sync]]))

(def host "http://localhost:3000/")

(defn api-get [url]
  (->
    host
    (str url)
    (js/fetch (clj->js {:method "GET" :headers {"Content-Type" "application/json"}}))))
