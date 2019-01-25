(ns attendance-app.scenes.login-screen
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [attendance-app.colors :refer [colors]]))

(def ReactNative (js/require "react-native"))
(def MaterialKit (js/require "react-native-material-kit"))
(def typography (.-material (js/require "react-native-typography")))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))

(def styles
  {:containe   {:flex            1 :background-color "black" :color "white" :align-items "center"
                :justify-content "center"}
   :text-edit  {:paddingVertical 20 :width 220}
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
    #(dispatch [:check-access-token navigate])

    [view {:style (:container styles)}
     [text-edit {:placeholder    "Enter your Access Token"
                 :on-text-change #(dispatch [:set-access-token %])}]]))
