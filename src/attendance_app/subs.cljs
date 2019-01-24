(ns attendance-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(defn- compose-to-marked [days]
  (into (sorted-map) (map #(hash-map % {:selected true :selectedColor "green"}) days)))

(reg-sub :list-attendants (fn [db _] (:attendants db)))
(reg-sub :list-attendances-days (fn [{:keys [attendances-days]} _] (compose-to-marked attendances-days)))
(reg-sub :attendant-first-name (fn [db _] (:attendant-first-name db)))
(reg-sub :attendant-last-name (fn [db _] (:attendant-last-name db)))
(reg-sub :get-attendant (fn [{attendant :attendant} _] attendant))