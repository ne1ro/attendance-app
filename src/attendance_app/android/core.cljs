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

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
      (.alert (.-Alert ReactNative) title))

(defn attendant-row [{:id id}]
  [view {:style {:flex 1 :flex-direction "row"}}
   [view {:style {:flex 0.2 :background-color "red"}}
    [text {:style {:font-size 20 :text-align "center"}} "1"]]

   [view {:style {:flex 0.4 :background-color "powderblue"}}
    [text {:style {:font-size 20 :text-align "center"}} "First"]]

   [view {:style {:flex 0.4 :background-color "powderblue"}}
    [text {:style {:font-size 20 :text-align "center"}} "Last"]]])

(defn app-root []
  (let [attendants (subscribe [:get-attendants])]
    #([toolbar {:title "Attendances"}]
      [view {:style {:flex 1 :flex-direction "row"}}
       [flat-list {:data #js[attendants] :render-item (fn [item] (r/as-element [text item]))}]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "AttendanceApp" #(r/reactify-component app-root)))
