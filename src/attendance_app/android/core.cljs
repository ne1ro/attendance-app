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
(def image (r/adapt-react-class (.-Image ReactNative)))

(def menu-img (js/require "./images/menu.png"))
(def colors
  {:primary "#3F51B5" :dark-primary "#303F9F" :light-primary "#C5CAE9" :accent "#FF5252"
   :text "#212121" :secondary-text "#757575" :divider "#BDBDBD"})

(defn alert [title] (.alert (.-Alert ReactNative) title))

(defn ->clj [js-obj] (js->clj js-obj :keywordize-keys true))

(defn attendant-row [a]
  (let [{id :id first-name :first-name last-name :last-name} a]
    [view {:style
           {:flex 1 :flex-direction "row" :text-align "center" :align-self "stretch" :height 40 :padding 10 :padding-bottom 15}}
      [view {:style {:flex 0.2}}
        [text {:style {:text-align "center" :font-weight "bold" :font-size 20}} (-> id (+ 1) str)]]
      [view {:style {:flex 0.4}}
        [text {:style {:font-size 20}} first-name]]
      [view {:style {:flex 0.4}}
        [text {:style {:font-size 20}} last-name]]]))

(defn app-root []
  (let [attendants (subscribe [:get-attendants])]
    (fn []
      [view {:style {:flex 1 :justify-content "flex-start" :color (:text colors)}}

      [toolbar
       {:title "Attendants"
        :title-color "#FFFFFF"
        :nav-icon menu-img
        :actions [{:title "Calendar"}]
        :style {:height 48 :background-color (:primary colors) :align-items "stretch"}}]

      [flat-list
        {:data @attendants
         :key-extractor (fn [item index] (-> item ->clj :id str))
         :render-item (fn [a] (-> a (->clj) :item (attendant-row) (r/as-element)))}]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "AttendanceApp" #(r/reactify-component app-root)))
