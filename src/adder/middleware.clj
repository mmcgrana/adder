(ns adder.middleware)

(defn- log [msg & vals]
  (let [line (apply format msg vals)]
    (locking System/out (println line))))

(defn wrap-request-logging [handler]
  (fn [{:keys [request-method uri] :as req}]
    (let [start  (System/currentTimeMillis)
          resp   (handler req)
          finish (System/currentTimeMillis)
          total  (- finish start)]
      (log "request %s %s (%dms)" request-method uri total)
      resp)))

(defn wrap-bounce-favicon [handler]
  (fn [req]
    (if (= [:get "/favicon.ico"] [(:request-method req) (:uri req)])
      {:status 400
       :headers {}
       :body ""}
      (handler req))))
