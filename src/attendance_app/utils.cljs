(ns attendance-app.utils
  (:require [cljs-time.core :as time]
            [cljs-time.format :as time-format]))

; TODO: make it co-effect
(defn format-day [fmt day] (time-format/unparse (time-format/formatter fmt) day))
(defn current-day [fmt] (format-day fmt (time/now)))

(defn ->clj [js-obj] (js->clj js-obj :keywordize-keys true))
