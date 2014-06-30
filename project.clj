(defproject adder "0.0.1"
  :description "Add two numbers."
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [ring/ring-core "0.3.7"]
     [ring/ring-jetty-adapter "0.3.7"]
     [compojure "0.6.2"]
     [hiccup "0.2.6"]]
  :dev-dependencies
    [[lein-run "1.0.0"]
	[ring/ring-devel "0.3.7"]]
)
