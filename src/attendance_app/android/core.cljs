(ns attendance-app.android.core
  (:require
   [cljs-time.core :as time]
   [cljs-time.format :as time-format]
   [reagent.core :as r :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [attendance-app.android.attendant-form :refer [attendant-form]]
   [attendance-app.android.list-attendants :refer [list-attendants]]))

(def react-navigation (js/require "react-navigation"))
(def ReactNative (js/require "react-native"))
(def createStackNavigator (.-createStackNavigator react-navigation))
(def createAppContainer (.-createAppContainer react-navigation))
(def app-registry (.-AppRegistry ReactNative))

(defn format-day [fmt] (time-format/unparse (time-format/formatter fmt) (time/now)))

(def default-nav-options
  {:headerStyle {:backgroundColor "#3F51B5" :elevation 2} :headerTitleStyle {:color "#FFFFFF"}})

(def routes {:AttendantsList {:screen (r/reactify-component list-attendants)
                              :navigationOptions {:title (format-day "E d 'of' MMMM")}}
             :AttendantForm {:screen (r/reactify-component attendant-form)
                             :navigationOptions {:title "Create an attendant"}}})

(def app-navigator
  (createStackNavigator (clj->js routes)
                        (clj->js
                         {:initialRouteName "AttendantForm"
                          :initialRouteParams {:day (format-day "yyyy-MM-dd")}
                          :defaultNavigationOptions default-nav-options})))

(defn app-root [] [:> (createAppContainer app-navigator) {}])

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch [:list-attendants (format-day "yyyy-MM-dd")])
  (.registerComponent app-registry "AttendanceApp" (r/reactify-component app-root)))
