(ns attendance-app.utils
  (:require [cljs-time.core :as time]
            [cljs-time.format :as time-format]))

(defn current-day [fmt] (time-format/unparse (time-format/formatter fmt) (time/now)))
