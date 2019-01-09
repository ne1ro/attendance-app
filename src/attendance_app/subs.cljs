(ns attendance-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-greeting
 (fn [db _] (:greeting db)))

(reg-sub :get-attendants (fn [db _] (:attendants db)))
