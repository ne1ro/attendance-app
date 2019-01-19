(ns attendance-app.effects
  (:require [attendance-app.fetch]
            [re-frame.core :refer [reg-event-fx]]))

; TODO: get from config
(def host "http://10.0.2.2:3000/")

(reg-event-fx ::api-get
              (fn [{db :db} [_ url]]
                {:fetch
                     {:url        (str host url)
                      :on-success [:process-response]
                      :on-failure [:show-error]}
                 :db (assoc db :loading? true)}))

(reg-event-fx ::api-post
              (fn [_world [_ url body]]
                {:fetch {:method     "POST"
                         :url        (str host url)
                         :body       body
                         :on-success [:process-response]
                         :on-failure [:show-error]}}))
