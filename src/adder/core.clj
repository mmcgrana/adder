(ns adder.core
  (:use compojure.core)
  (:use hiccup.core)
  (:use hiccup.page-helpers)
  (:use adder.middleware)
  (:use ring.middleware.file)
  (:use ring.middleware.file-info)
  (:use ring.middleware.reload)
  (:use ring.middleware.stacktrace)
  (:require [ring.util.response :as resp]))

(defn view-layout [& content]
  (html
    (doctype :xhtml-strict)
    (xhtml-tag "en"
      [:head
        [:meta {:http-equiv "Content-type" :content "text/html; charset=utf-8"}]
        [:title "adder"]
        [:link {:href "/adder.css" :rel "stylesheet" :type "text/css"}]]
      [:body content])))

(defn view-input [& [a b]]
  (view-layout
    [:h2 "add two numbers"]
    [:form {:method "post" :action "/"}
      (if (and a b)
        [:p "those are not both numbers!"])
      [:input.math {:type "text" :name "a" :value a}] [:span.math " + "]
      [:input.math {:type "text" :name "b" :value b}] [:br]
      [:input {:type "submit" :value "add"}]]))

(defn view-output [a b sum]
  (view-layout
    [:h2 "two numbers added"]
    [:span.math a " + " b " = " sum] [:br]
    [:a {:href "/"} "add more numbers"]))

(defn parse-input [a b]
  [(Integer/parseInt a) (Integer/parseInt b)])

(defroutes app-core
  (GET "/" []
    (view-input))

  (POST "/" [a b]
    (try
      (let [[a b] (parse-input a b)
            sum   (+ a b)]
        (view-output a b sum))
      (catch NumberFormatException e
        (view-input a b))))

  (ANY "/*" [path]
    (resp/redirect "/")))

(def app
  (-> #'app-core
    (wrap-file "public")
    (wrap-file-info)
    (wrap-request-logging)
    (wrap-reload '[adder.middleware adder.core])
    (wrap-bounce-favicon)
    (wrap-stacktrace)))
