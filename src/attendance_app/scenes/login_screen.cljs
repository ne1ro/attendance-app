(ns attendance-app.scenes.login-screen
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [attendance-app.colors :refer [colors]]))

(def ReactNative (js/require "react-native"))
(def MaterialKit (js/require "react-native-material-kit"))
(def typography (.-material (js/require "react-native-typography")))
(def image-background (r/adapt-react-class (.-ImageBackground ReactNative)))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def bg (js/require "./images/bg.jpg"))

(def styles
  {:container  {:flex 1 :color "white" :align-items "center" :justify-content "center"}
   :text-edit  {:paddingVertical 20 :width 300}
   :title      (-> typography .-display1 clj->js (assoc :text-align :center))
   :text-input {:color (:white colors) :fontSize 24 :textAlign "center"}})

(def text-edit
  (-> MaterialKit
      .-mdl
      .-Textfield
      .textfield
      (.withStyle (clj->js (:text-edit styles)))
      (.withTextInputStyle (clj->js (:text-input styles)))
      .build
      r/adapt-react-class))

(defn login-screen [{navigation :navigation}]
  (let [navigate (.-navigate navigation)]
    ;#(dispatch [:check-access-token navigate])

    [image-background {:style (:container styles) :source bg}
     [text {:style (:title styles)} "Please enter your Access Code"]
     [text-edit {:placeholder    ""
                 :on-text-change #(dispatch [:set-access-token %])}]]))
