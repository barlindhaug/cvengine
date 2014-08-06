(ns cvengine.core-test
  (:require [cvengine.core :refer :all])
  (:use midje.sweet))

(facts "about find similar"
       (fact (find-similar ["clojure" "java" "java 7" "Java SE"]) => ["clojure" "Java" "Java" "Java"]))
