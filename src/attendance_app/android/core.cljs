(ns attendance-app.android.core
  (:require
   [reagent.core :as r :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [attendance-app.subs]
   [attendance-app.events]
   [attendance-app.utils :refer [current-day]]
   [attendance-app.scenes.attendant :refer [attendant]]
   [attendance-app.scenes.login-screen :refer [login-screen]]
   [attendance-app.scenes.attendant-form :refer [attendant-form]]
   [attendance-app.scenes.attendances-calendar :refer [attendances-calendar]]
   [attendance-app.scenes.list-attendants :refer [list-attendants]]))

(def react-navigation (js/require "react-navigation"))
(def ReactNative (js/require "react-native"))
(def createStackNavigator (.-createStackNavigator react-navigation))
(def createAppContainer (.-createAppContainer react-navigation))
(def app-registry (.-AppRegistry ReactNative))

(def default-nav-options
  {:headerStyle      {:backgroundColor "#3F51B5" :elevation 4 :overflow "visible"}
   :headerTitleStyle {:color "white"}
   :headerTintColor  "white"
   :id               nil
   :day              (current-day "E d 'of' MMMM")})

(def routes {:Login               {:screen            (r/reactify-component login-screen)
                                   :navigationOptions {:header nil}}
             :AttendantsList      {:screen            (r/reactify-component list-attendants)
                                   :navigationOptions {:title "This day attendants"}}
             :Attendant           {:screen            (r/reactify-component attendant)
                                   :navigationOptions {:title "Attendant"}}
             :AttendancesCalendar {:screen            (r/reactify-component attendances-calendar)
                                   :navigationOptions {:title "Attendances Calendar"}}
             :AttendantForm       {:screen            (r/reactify-component attendant-form)
                                   :navigationOptions {:title "Create an attendant"}}})

(def app-navigator
  (createStackNavigator
   (clj->js routes)
   (clj->js
    {:initialRouteName         "Login"
     :initialRouteParams       {:day (current-day "yyyy-MM-dd")}
     :defaultNavigationOptions default-nav-options})))

(defn app-root [] [:> (createAppContainer app-navigator) {}])

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "AttendanceApp" (r/reactify-component app-root)))
