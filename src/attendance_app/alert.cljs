(ns attendance-app.alert (:require [reagent.core :as r :refer [atom]]))

(def ReactNative (js/require "react-native"))
(defn alert [title message] (prn message) (.alert (.-Alert ReactNative) title message))
