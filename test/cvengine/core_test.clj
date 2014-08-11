(ns cvengine.core-test
  (:require [cvengine.core :refer :all])
  (:use midje.sweet))

(facts "about find similar"
       (fact "java 7 and java SE should be Java"
             (find-similar ["clojure" "Java" "Java 7" "Java SE"]) => ["clojure" "Java" "Java" "Java"])
       (fact "javascript should not be replaced by Java"
             (find-similar ["JavaScript"]) => ["JavaScript"])
       (fact "javascript should be spelled JavaScript"
             (find-similar ["javascript"]) => ["JavaScript"]))
