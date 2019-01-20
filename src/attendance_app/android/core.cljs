(ns attendance-app.android.core
  (:require
    [reagent.core :as r :refer [atom]]
    [re-frame.core :refer [subscribe dispatch dispatch-sync]]
    [attendance-app.utils :refer [current-day]]
    [attendance-app.android.attendant-form :refer [attendant-form]]
    [attendance-app.android.list-attendants :refer [list-attendants]]))

(def react-navigation (js/require "react-navigation"))
(def ReactNative (js/require "react-native"))
(def createStackNavigator (.-createStackNavigator react-navigation))
(def createAppContainer (.-createAppContainer react-navigation))
(def app-registry (.-AppRegistry ReactNative))

(def default-nav-options
  {:headerStyle {:backgroundColor "#3F51B5" :elevation 4 :shadowOpacity 100}
   :headerTitleStyle {:color "#FFFFFF"}
   :day (current-day "E d 'of' MMMM")})

(def routes {:AttendantsList {:screen (r/reactify-component list-attendants)
                              :navigationOptions {:title (current-day "E d 'of' MMMM")}}
             :AttendantForm {:screen (r/reactify-component attendant-form)
                             :navigationOptions {:title "Create an attendant"}}})

(def app-navigator
  (let [routing (subscribe :routing)]
    (createStackNavigator
      (clj->js routes)
      (clj->js
        {:initialRouteName         "AttendantsList"
         :initialRouteParams       {:day (current-day "yyyy-MM-dd")}
         :defaultNavigationOptions default-nav-options}))))

(defn app-root [] [:> (createAppContainer app-navigator) {}])

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch [:list-attendants (current-day "yyyy-MM-dd")])
  (.registerComponent app-registry "AttendanceApp" (r/reactify-component app-root)))
