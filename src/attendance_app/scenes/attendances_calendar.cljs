(ns attendance-app.scenes.attendances-calendar
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [dispatch subscribe]]
            [attendance-app.colors :refer [colors]]
            [attendance-app.utils :refer [->clj]]))

(def ReactNative (js/require "react-native"))
(def typography (.-material (js/require "react-native-typography")))
(def calendar (r/adapt-react-class (.-Calendar (js/require "react-native-calendars"))))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))

(def styles
  {:container {:flex 1 :color (:text colors) :padding-horizontal 15 :padding-vertical 15}
   :display1  (-> typography .-display1 ->clj (assoc :text-align "center"))
   :calendar  {:padding-top 15}})

(defn attendances-calendar [{navigation :navigation}]
  (let [attendances-days (subscribe [:list-attendances-days])
        navigate (.-navigate navigation)]
    (dispatch [:list-attendances-days navigate])
    [view {:style (:container styles)}
     [text {:style (:display1 styles)} "Attendances by days"]

     [calendar
      {:min-date     "2018-09-01"
       :style        (:calendar styles)
       :first-day    1
       :marked-dates @attendances-days
       :on-day-press #(dispatch [:list-attendants (.-dateString %) navigate])}]]))