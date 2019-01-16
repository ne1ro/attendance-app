(ns attendance-app.api)

; TODO: get from config
(def host "http://10.0.2.2:3000/")

(defn- handle-response [resp success-handler]
  (->
   resp
   .json
   (.then (fn [data] (success-handler (js->clj data :keywordize-keys true))))))

(defn- fetch [url method params success-handler err-handler]
  (let [default-params {:method method :headers {"Content-Type" "application/json"}}]
    (->
      host
      (str url)
      (js/fetch (clj->js (merge default-params params)))
      (.then #(handle-response % success-handler))
      (.catch #(-> % .-message err-handler)))))

(defn- api-get [url success-handler err-handler]
  (fetch url "GET" {} success-handler err-handler))

(defn- api-post [url body success-handler err-handler]
  (fetch url "POST" {:body (js/JSON.stringify (clj->js body))} success-handler err-handler))

(defn list-attendants [day success-handler err-handler]
  (api-get (str "attendances/" day) success-handler err-handler))

(def create-attendant (partial api-post "attendants"))
