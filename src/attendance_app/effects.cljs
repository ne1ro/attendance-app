(ns attendance-app.effects
  (:require [day8.re-frame.http-fx]
            [re-frame.core :refer [reg-event-fx]]))

; TODO: get from config
(def host "http://10.0.2.2:3000/")

(re-frame/reg-event-fx
  ::http-post
  (fn [_world [_ val]]
    {:http-xhrio {:method          :post
                  :uri             host
                  :params          data
                  :timeout         2500
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::process-response]
                  :on-failure      [::show-error]}}))
