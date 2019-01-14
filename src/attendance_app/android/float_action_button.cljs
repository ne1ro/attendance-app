(ns attendance-app.android.float-action-button
  (:require [reagent.core :as r :refer [atom]]))

(def ReactNative (js/require "react-native"))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def material-kit (js/require "react-native-material-kit"))

(defn- button-component [press-handler] (-> material-kit
                                            .-MKButton
                                            (.accentColoredFlatButton)
                                            (.withBackgroundColor "#FF5252")
                                            (.withMaskColor "transparent")
                                            (.withOnPress press-handler)
                                            (.withStyle #js {:position "absolute"
                                                             :right 16
                                                             :bottom 16
                                                             :elevation 2
                                                             :width 56
                                                             :height 56
                                                             :borderRadius 28})
                                            (.build)
                                            (r/adapt-react-class)))

(defn fab [press-handler]
  [(button-component press-handler)
   [text {:style {:font-size 28 :font-weight "400" :color "#FFF"}} "+"]])
