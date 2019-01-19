(ns attendance-app.effects
  (:require [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [re-frame.core :refer [reg-event-fx]]))

; TODO: get from config
(def host "http://10.0.2.2:3000/")

(reg-event-fx ::api-get
              (fn [{db :db} [_ url]]
                (prn url)
                {:http-xhrio
                     {:method          :get
                      :url             (str host url)
                      :timeout         1000
                      :format          (ajax/json-response-format)
                      :response-format (ajax/json-response-format {:keywords? true})
                      :on-success      [:process-response]
                      :on-failure      [::show-error]}
                 :db (assoc db :loading? true)}))

(reg-event-fx ::api-post
              (fn [_world [_ data]]
   {:http-xhrio {:method          :post
                 :uri             host
                 :params          data
                 :timeout         2500
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::process-response]
                 :on-failure      [::show-error]}}))
