(ns attendance-app.android.core
  (:require
    [reagent.core :as r :refer [atom]]
    [re-frame.core :refer [subscribe dispatch dispatch-sync]]
    [attendance-app.android.list-attendants :refer [list-attendants]]))

(def react-navigation (js/require "react-navigation"))
(def ReactNative (js/require "react-native"))
(def createStackNavigator (.-createStackNavigator react-navigation))
(def createAppContainer (.-createAppContainer react-navigation))
(def app-registry (.-AppRegistry ReactNative))

(def default-nav-options
  {:headerStyle {:backgroundColor "#3F51B5"} :headerTitleStyle {:color "#FFFFFF"}})

(def app-navigator
  (createStackNavigator
    (clj->js {:Home {:screen (r/reactify-component list-attendants)
     :navigationOptions {:title "Attendants"}}})
    (clj->js {:defaultNavigationOptions default-nav-options})))

(defn app-root [] [:> (createAppContainer app-navigator) {}])

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch [:list-attendants])
  (.registerComponent app-registry "AttendanceApp" (r/reactify-component app-root)))
