(ns attendance-app.android.list-attendants (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe]]
            [attendance-app.android.float-action-button :refer [fab]]
            [attendance-app.events]
            [attendance-app.subs]))

(defn- ->clj [js-obj] (js->clj js-obj :keywordize-keys true))

(def ReactNative (js/require "react-native"))
(def Typography (js/require "react-native-typography"))
(def MaterialKit (js/require "react-native-material-kit"))
(def material (.-material Typography))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def toolbar (r/adapt-react-class (.-ToolbarAndroid ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))

(def menu-img (js/require "./images/menu.png"))
(def colors
  {:primary "#3F51B5" :dark-primary "#303F9F" :light-primary "#C5CAE9" :accent "#FF5252"
   :text "#212121" :secondary-text "#757575" :divider "#BDBDBD"})

(defn attendant-row [a]
  (let [{first-name :firstName last-name :lastName} a]
    [view {:style
           {:flex 1
            :flex-direction "row"
            :text-align "center"
            :align-self "stretch"
            :padding-horizontal 40
            :padding-vertical 15
            :elevation 1}}
     [text {:style (:display2 material)} (str first-name " " last-name)]]))

(defn list-attendants [props]
  (let [attendants (subscribe [:list-attendants])]
    (fn []
      [view {:style {:flex 1 :justify-content "flex-start" :color (:text colors) :padding-bottom 20}}

       ; [toolbar
       ;  {:title "Attendants"
       ;   :title-color "#FFFFFF"
       ;   :nav-icon menu-img
       ;   :actions [{:title "Calendar"}]
       ;   :style {:height 48 :background-color (:primary colors) :align-items "stretch" :elevation 3}}]

       [fab]

       [flat-list
        {:data @attendants
         :key-extractor (fn [item index] (-> item ->clj :id str))
         :render-item (fn [a] (-> a (->clj) :item (attendant-row) (r/as-element)))}]])))