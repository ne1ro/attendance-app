(ns attendance-app.alert (:require [reagent.core :as r :refer [atom]]))

(def Alert (.-Alert (js/require "react-native")))

(defn alert
  ([title message] (.alert Alert title message))
  ([title message on-ok on-cancel]
   (.alert
     Alert
     title
     message
     (clj->js [{:text "OK" :onPress on-ok} {:text "Cancel" :onPress on-cancel :style "cancel"}])
     (clj->js {:cancelable false}))))