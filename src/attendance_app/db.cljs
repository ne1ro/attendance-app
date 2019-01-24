(ns attendance-app.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::attendants list?)
(s/def ::app-db
  (s/keys :req-un []))

;; initial state of app-db
(def app-db
  {:attendants [] :loading? false :attendant-first-name "" :attendant-last-name "" :attendant {} :attendances-days []})
