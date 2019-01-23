(ns attendance-app.scenes.attendant
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [dispatch subscribe]]
            [attendance-app.colors :refer [colors]]))

(def ReactNative (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def typography (.-material (js/require "react-native-typography")))

(def styles
  {:subheading (-> typography .-subheading js->clj (assoc :text-align "center"))
   :title      (-> typography .-title js->clj (assoc :text-align "center"))
   :display1   (-> typography .-display1 js->clj (assoc :text-align "center" "color" (:white colors)))
   :headline   (.-headline typography)
   :container  {:flex 1 :justify-content "flex-start" :align-items "center" :width "100%"}
   :header     {:background-color (:accent colors) :width "100%" :height "25%"
                :justify-content  "center"}
   :footer     {:width "100%" :height "75%" :justify-content "center"}})

(defn attendant [{navigation :navigation}]
  (dispatch [:get-attendant (.getParam navigation "id" nil)])
  (let [attendant (subscribe [:get-attendant])]
    [view {:style (:container styles)}
     [view {:style (:header styles)}
      [text {:style (:display1 styles)} (str (:firstName @attendant) " " (:lastName @attendant))]]

     [view {:style (:footer styles)}]]))