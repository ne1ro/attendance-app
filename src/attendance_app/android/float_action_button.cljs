(ns attendance-app.android.float-action-button
  (:require [reagent.core :as r :refer [atom]]))

(def ReactNative ^:private (js/require "react-native"))
(def text ^:private (r/adapt-react-class (.-Text ReactNative)))
(def material-kit ^:private (js/require "react-native-material-kit"))
(def MDButton ^:private (.-MKButton material-kit))

(def button-component (-> MDButton
                          (.accentColoredFlatButton)
                          (.withBackgroundColor "#FF5252")
                          (.withMaskColor "transparent")
                          (.withStyle #js {:position "absolute"
                                           :right 16
                                           :bottom 16
                                           :elevation 2
                                           :width 56
                                           :height 56
                                           :borderRadius 28})
                          (.build)
                          (r/adapt-react-class)))

(defn fab []
  [button-component
   [text {:style {:font-size 28 :font-weight "400" :color "#FFF"}} "+"]])
