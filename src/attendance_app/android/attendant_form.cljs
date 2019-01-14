(ns attendance-app.android.attendant-form
  (:require [reagent.core :as r :refer [atom]]
            [attendance-app.colors :refer [colors]]))

(def ReactNative (js/require "react-native"))
(def MaterialKit (js/require "react-native-material-kit"))
(def Typography (js/require "react-native-typography"))
(def material (.-material Typography))
(def view (r/adapt-react-class (.-View ReactNative)))

(def text-edit (-> MaterialKit .-MKTextField r/adapt-react-class))

(defn attendant-form []
  (fn []
    [view {:style {:flex 1 :align-items "center" :background-color (:accent colors) :color "#FFFFFF" :padding 20}}

     [text-edit {:placeholder "First Name"}]
     [text-edit {:placeholder "Last Name"}]]))
