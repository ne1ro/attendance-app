(ns attendance-app.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [attendance-app.events]
            [attendance-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def toolbar (r/adapt-react-class (.-ToolbarAndroid ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def menu-img (js/require "./images/menu.png"))

(defn alert [title]
      (.alert (.-Alert ReactNative) title))

(def colors
  {:primary "#3F51B5" :dark-primary "#303F9F" :light-primary "#C5CAE9" :accent "#FF5252"
   :text "#212121" :secondary-text "#757575" :divider "#BDBDBD"})

; (defn attendant-row [{:id id}]
;   [view {:style {:flex 1 :flex-direction "row"}}
;    [view {:style {:flex 0.2 :background-color "red"}}
;     [text {:style {:font-size 20 :text-align "center"}} "1"]]

;    [view {:style {:flex 0.4 :background-color "powderblue"}}
;     [text {:style {:font-size 20 :text-align "center"}} "First"]]

;    [view {:style {:flex 0.4 :background-color "powderblue"}}
;     [text {:style {:font-size 20 :text-align "center"}} "Last"]]])

(defn app-root []
  (let [attendants (subscribe [:get-attendants])]
    (fn [] [view {:style {:flex 1 :justify-content "flex-start" :color (:text colors)}}
      [toolbar
       {:title "Attendants"
        :title-color "#FFFFFF"
        :navIcon menu-img
        :style {:height 60 :background-color (:primary colors) :align-items "stretch"}}]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "AttendanceApp" #(r/reactify-component app-root)))
