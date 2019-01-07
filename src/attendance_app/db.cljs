(ns attendance-app.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::greeting]))

;; initial state of app-db
(def app-db {
  :greeting "Attendance App"
  :attendants [
    {:id 0 :first-name "Vasya" :last-name "Herasse"}
    {:id 1 :first-name "Kekarium"  :last-name "Pekarium"}
    {:id 2 :first-name "Third"  :last-name "Name"}
  ]})
