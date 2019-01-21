(ns attendance-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [attendance-app.events]
            [attendance-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn app-root []
  (fn []
    [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
     [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                           :on-press #(alert "Attended!")}
      [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "Attend"]]]))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "AttendanceApp" #(r/reactify-component app-root)))
