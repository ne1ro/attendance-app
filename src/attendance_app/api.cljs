(ns attendance-app.api)

; TODO: get from config
(def host "http://10.0.2.2:3000/")

(def headers {"Content-Type" "application/json"})

(defn- handle-response [resp success-handler]
  (->
   resp
   .json
   (.then (fn [data] (success-handler (js->clj data :keywordize-keys true))))))

(defn- api-get [url success-handler err-handler]
  (->
   host
   (str url)
   (js/fetch (clj->js {:method "GET" :headers headers}))
   (.then #(handle-response % success-handler))
   (.catch (fn [err] (-> err .-message err-handler)))))

(defn- api-post [url body success-handler err-handler]
  (->
   host
   (str url)
   (js/fetch
    (clj->js {:method "POST" :headers headers :body (js/JSON.stringify (clj->js body))}))
   (.then #(handle-response % success-handler))
   (.catch #(-> % .-message err-handler))))

(def list-attendants (partial api-get "attendances/"))
(def create-attendant (partial api-post "attendants"))
