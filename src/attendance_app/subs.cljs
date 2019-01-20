(ns attendance-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub :list-attendants (fn [db _] (:attendants db)))
(reg-sub :attendant-first-name (fn [db _] (:attendant-first-name db)))
(reg-sub :attendant-last-name (fn [db _] (:attendant-last-name db)))
(reg-sub :routing (fn [db _] (:routing db)))