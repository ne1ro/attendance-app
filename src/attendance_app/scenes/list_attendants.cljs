(ns attendance-app.scenes.list-attendants
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [dispatch subscribe]]
            [attendance-app.float-action-button :refer [fab]]
            [attendance-app.colors :refer [colors]]
            [attendance-app.utils :refer [format-day ->clj]]
            [clojure.string :as str]
            [cljs-time.core :as time]
            [cljs-time.format :as format]))

(def ReactNative (js/require "react-native"))
(def typography (.-material (js/require "react-native-typography")))
(def gesture-recognizer (r/adapt-react-class (.-default (js/require "react-native-swipe-gestures"))))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def toolbar (r/adapt-react-class (.-ToolbarAndroid ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def touchable-feedback (r/adapt-react-class (.-TouchableNativeFeedback ReactNative)))

(def styles
  {:subheading    (-> typography .-subheading ->clj (assoc :text-align "center"))
   :title         (-> typography .-title ->clj (assoc :text-align "center"))
   :display1      (-> typography .-display1 ->clj (assoc :text-align "center"))
   :headline      (.-headline typography)
   :container     {:flex 1 :color (:text colors) :padding-horizontal 20 :padding-vertical 15}
   :dot           {:height 20 :width 20 :border-radius 10 :elevation 2}
   :fab           {:font-size 28 :font-weight "400" :color "#FFF"}
   :attendant-row {:flex-direction "row" :padding-horizontal 15 :padding-vertical 15}
   :dot-container {:flex-direction     "column"
                   :flex               0.15
                   :align-items        "flex-end"
                   :justify-content    "center"
                   :padding-horizontal 8}})

(defn- set-dot-style [status]
  (let [color (case status "attended" (:complementary colors) (:accent colors))]
    (-> styles :dot (assoc :background-color color))))

(defn- attendant-row [{:keys [id firstName lastName status] :as attendant} day navigate]
  [gesture-recognizer
   {:style          (:attendant-row styles)
    :on-swipe-left  #(dispatch [:show-delete-dialogue id])
    :on-swipe-right #(dispatch [:show-delete-dialogue id])}

   [view {:style (:dot-container styles)}
    [touchable-feedback {:on-press #(dispatch [:toggle-attendance attendant day])}
     [view {:style (set-dot-style status)}]]]

   [touchable-feedback
    {:on-press      #(dispatch [:toggle-attendance attendant day])
     :on-long-press #(dispatch [:get-attendant navigate id])}
    [view {:style {:flex-direction "column" :flex 0.85}}
     [text {:style (:headline styles)} (str lastName " " firstName)]]]])

(defn list-attendants [{navigation :navigation}]
  (let [attendants (subscribe [:list-attendants])
        day (.getParam navigation "day" "2019-01-01")
        title (->> day
                   (format/parse (format/formatter "yyyy-MM-dd"))
                   (format-day "E d 'of' MMMM"))
        navigate (.-navigate navigation)]

    [view {:style (:container styles)}
     (let [floating-action-button (fab #(dispatch [:navigate navigate "AttendantForm"]))]
       [floating-action-button [text {:style (:fab styles)} "+"]])

     (if (empty? @attendants)
       [view
        [text {:style (:title styles)} "There is no one to attend :("]
        [text {:style (-> styles :subheading (assoc :padding-top 15))}
         "Would you like to add someone?"]]

       [view
        [touchable-feedback {:on-press #(dispatch [:list-attendances-days navigate])}
         [text {:style (:display1 styles)} title]]

        [flat-list
         {:data          @attendants
          :style         {:padding-top 15}
          :key-extractor (fn [item _] (-> item ->clj :id str))
          :render-item   (fn [a] (-> a (->clj) :item (attendant-row day navigate) (r/as-element)))}]])]))
