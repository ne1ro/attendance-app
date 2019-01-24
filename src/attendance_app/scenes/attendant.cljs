(ns attendance-app.scenes.attendant
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [dispatch subscribe]]
            [attendance-app.utils :refer [->clj]]
            [attendance-app.colors :refer [colors]]))

(def ReactNative (js/require "react-native"))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def typography (.-material (js/require "react-native-typography")))

(def styles
  {:display1       (-> typography .-display1 ->clj (assoc :text-align "center" :color (:white colors)))
   :body2          (-> typography .-body1 ->clj)
   :container      {:flex 1 :justify-content "flex-start" :align-items "center" :width "100%" :elevation 0}
   :header         {:background-color (:accent colors) :width "100%" :height "33%" :justify-content "center" :elevation 1}
   :align-left     {:text-align "left"}
   :align-right    {:text-align "right"}
   :cell           {:flex 0.5}
   :row            {:flex-direction "row" :align-items "center" :padding 10}
   :bold           {:font-weight "bold"}
   :secondary-text {:color (:secondary-text colors)}
   :footer         {:width "100%" :height "67%" :flex 1 :padding 15 :flex-direction "row" :justify-content "flex-start"}})

(defn- pick-styles [ks] (apply merge (-> styles (select-keys ks) vals)))

(defn- field-cell [caption value]
  [view {:style (:row styles)}
   [text {:style (pick-styles [:body2 :align-left :secondary-text])} (str caption ": ")]
   [text {:style (pick-styles [:body2 :align-right :bold])} value]])

(defn attendant [_]
  (let [attendant (subscribe [:get-attendant])]
    (if @attendant
      [view {:style (:container styles)}
       [view {:style (:header styles)}
        [text {:style (:display1 styles)} (str (:firstName @attendant) " " (:lastName @attendant))]]

       [view {:style (:footer styles)}
        [view {:style (:cell styles)}
         [field-cell "First name" (:firstName @attendant)]
         [field-cell "Last name" (:lastName @attendant)]]

        [view {:style (:cell styles)}
         [field-cell "Attendance percentage" (str (:attendancePercentage @attendant) "%")]
         [field-cell "Total attendances" (:attendancesCount @attendant)]
         [field-cell "Total days" (:daysCount @attendant)]]]]

      [text (:style (:display1 styles)) "There is no such attendant"])))