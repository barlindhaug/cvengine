(defproject cvengine "0.1.0-SNAPSHOT"
  :dependencies [
                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/data.json "0.2.5"]
                  [clj-http "0.9.2"]
                  ]
  :dev-dependencies []
  :main ^:skip-aot cvengine.core
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins [[lein-midje "3.1.3"]]}
             :uberjar {:aot :all}})
