(ns adder.core-test
  (:use clojure.test)
  (:use adder.core))

(deftest parse-input-valid
  (is (= [1 2] (parse-input "1" "2"))))

(deftest parse-input-invalid
  (is (thrown? NumberFormatException
    (parse-input "foo" "bar"))))

(deftest view-output-valid
  (let [html (view-output 1 2 3)]
    (is (re-find #"two numbers added" html))))

(deftest handle-input-valid
  (let [resp (handler {:uri "/" :request-method :get})]
    (is (= 200 (:status resp)))
    (is (re-find #"add two numbers" (:body resp)))))

(deftest handle-add-valid
  (let [resp (handler {:uri "/" :request-method :post
                       :params {"a" "1" "b" "2"}})]
    (is (= 200 (:status resp)))
    (is (re-find #"1 \+ 2 = 3" (:body resp)))))

(deftest handle-add-invalid
  (let [resp (handler {:uri "/" :request-method :post
                       :params {"a" "foo" "b" "bar"}})]
    (is (= 200 (:status resp)))
    (is (re-find #"those are not both numbers" (:body resp)))))

(deftest handle-catchall
  (let [resp (handler {:uri "/foo" :request-method :get})]
    (is (= 302 (:status resp)))
    (is (= "/" (get-in resp [:headers "Location"])))))
