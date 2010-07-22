(ns adder.core
  (:use compojure.core)
  (:use hiccup.core)
  (:use hiccup.page-helpers)
  (:use ring.middleware.reload)

(defn view-layout [& content]
  (html
    (doctype :xhtml-strict)
    (xhtml-tag "en"
      [:head
        [:meta {:http-equiv "Content-type" :content "text/html; charset=utf-8"}]
        [:title "adder"]]
      [:body content])))

(defn view-input []
  (view-layout
    [:h2 "add two numbers"]
    [:form {:method "post" :action "/"}
      [:input {:type "text" :name "a"}] " + "
      [:input {:type "text" :name "b"}] [:br]
      [:input {:type "submit" :value "add"}]]))

(defn view-output [a b sum]
  (view-layout
    [:h2 "two numbers added"]
    [:p "the sum of " a " and " b " is " sum]
    [:a {:href "/"} "add more numbers"]))

(defn parse-input [a b]
  [(Integer/parseInt a) (Integer/parseInt b)])

(defroutes app-core
  (GET "/" []
    (view-input))

  (POST "/" [a b]
    (let [[a b] (parse-input a b)
          sum   (+ a b)]
      (view-output a b sum))))

(def app
  (-> #'app-core
    (wrap-reload '[adder.core])))
