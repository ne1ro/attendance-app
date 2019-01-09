(ns attendance-app.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::greeting]))

;; initial state of app-db
(def app-db {:greeting "Attendance App"
             :attendants [{:id 0 :first-name "Vasya" :last-name "Herasse"}
                          {:id 1 :first-name "Kekarium"  :last-name "Pekarium"}
                          {:id 2 :first-name "Third"  :last-name "Name"}
                          {:id 3 :first-name "Vasya" :last-name "Herasse"}
                          {:id 4 :first-name "Kekarium"  :last-name "Pekarium"}
                          {:id 5 :first-name "Third"  :last-name "Name"}
                          {:id 6 :first-name "Vasya" :last-name "Herasse"}
                          {:id 7 :first-name "Kekarium"  :last-name "Pekarium"}
                          {:id 8 :first-name "Third"  :last-name "Name"}
                          {:id 9 :first-name "Vasya" :last-name "Herasse"}
                          {:id 10 :first-name "Kekarium"  :last-name "Pekarium"}
                          {:id 11 :first-name "Third"  :last-name "Name"}
                          {:id 12 :first-name "Vasya" :last-name "Herasse"}
                          {:id 13 :first-name "Kekarium"  :last-name "Pekarium"}
                          {:id 14 :first-name "Third"  :last-name "Name"}
                          {:id 15 :first-name "Vasya" :last-name "Herasse"}
                          {:id 16 :first-name "Kekarium"  :last-name "Pekarium"}
                          {:id 17 :first-name "Third"  :last-name "Name"}]})
