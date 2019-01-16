(ns attendance-app.android.attendant-form
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [attendance-app.colors :refer [colors]]))

(def ReactNative (js/require "react-native"))
(def MaterialKit (js/require "react-native-material-kit"))
(def Typography (js/require "react-native-typography"))
(def material (.-material Typography))
(def view (r/adapt-react-class (.-View ReactNative)))

(def text-edit
  (-> MaterialKit
      .-mdl
      .-Textfield
      .textfield
      (.withStyle #js{:paddingVertical 20 :width 220})
      (.withTextInputStyle #js{:color (:white colors) :fontSize 24 :textAlign "center"})
      .build
      r/adapt-react-class))

(def submit-button
  (-> MaterialKit
      .-MKButton
      .accentColoredFlatButton
      (.withBackgroundColor (:accent colors))
      (.withText "SUBMIT")
      (.withStyle #js{:width 120 :marginTop 20})
      (.withTextStyle #js{:color (:white colors) :fontSize 18})
      (.build)
      (r/adapt-react-class)))

(defn attendant-form [{navigation :navigation}]
  (fn []
    [view {:style
           {:flex 1
            :align-items "center"
            :background-color (:dark-primary colors)
            :color (:white colors) :padding 20}}

     [text-edit
      {:placeholder "First Name" :ref "1" :onTextChange #(dispatch [:set-attendant-first-name %])}]

     [text-edit
      {:placeholder "Last Name" :ref "2" :onTextChange #(dispatch [:set-attendant-last-name %])}]

     [submit-button {:on-press #(dispatch [:create-attendant (.-navigate navigation)])}]]))
