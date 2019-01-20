(ns attendance-app.android.list-attendants
  (:require [reagent.core :as r :refer [atom]]
                                                     [re-frame.core :refer [subscribe]]
                                                     [attendance-app.android.float-action-button :refer [fab]]
                                                     [attendance-app.colors :refer [colors]]
                                                     [attendance-app.events]
                                                     [attendance-app.subs]))

(defn- ->clj [js-obj] (js->clj js-obj :keywordize-keys true))

(def ReactNative (js/require "react-native"))
(def typography (.-material (js/require "react-native-typography")))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def toolbar (r/adapt-react-class (.-ToolbarAndroid ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))

(def styles
  {:subheading    (-> typography .-subheading ->clj (assoc :text-align "center"))
   :title         (-> typography .-title ->clj (assoc :text-align "center"))
   :display1      (-> typography .-display1 ->clj (assoc :text-align "center"))
   :headline      (.-headline typography)
   :container     {:flex 1 :color (:text colors) :padding-horizontal 20 :padding-vertical 15}
   :dot-container {:flex-direction     "column"
                   :flex               0.25
                   :align-items        "flex-end"
                   :justify-content    "center"
                   :padding-horizontal 8}
   :dot           {:height 16 :width 16 :border-radius 8}
   :attendant-row {:flex-direction "row" :padding-horizontal 20 :padding-vertical 15}})

(defn- set-dot-style [status]
  (let [color (case status "attended" (:complementary colors) (:accent colors))]
    (-> styles :dot (assoc :background-color color))))

(defn- attendant-row [{:keys [firstName lastName status]}]
  [view {:style (:attendant-row styles)}
     [view {:style (:dot-container styles)} [view {:style (set-dot-style status)}]]

     [view {:style {:flex-direction "column" :flex 0.75}}
      [text {:style (:headline styles)} (str lastName " " firstName)]]])

(defn list-attendants [{navigation :navigation}]
  (let [attendants (subscribe [:list-attendants])
        day (.getParam navigation "day" "2019-02-01")]
    [view {:style (:container styles)}
       [fab (partial (.-navigate navigation) "AttendantForm")]

       (if (or (empty? @attendants) (not (vector? @attendants)))
         [view
          [text {:style (:title styles)} "There is no one to attend :("]
          [text {:style (-> styles :subheading (assoc :padding-top 20))}
           "Would you like to add someone?"]]

         [view
          [text {:style (:display1 styles)} "Today's attendants"]
          [flat-list
           {:data          @attendants
            :style         {:padding-top 20}
            :key-extractor (fn [item _] (-> item ->clj :id str))
            :render-item   (fn [a] (-> a (->clj) :item (attendant-row) (r/as-element)))}]])]))