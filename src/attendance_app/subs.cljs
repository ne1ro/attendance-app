(ns attendance-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub :list-attendants (fn [db _] (:attendants db)))
