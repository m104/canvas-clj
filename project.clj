(defproject canvas-clj "0.1.1"
  :description "A simple simulator of the Canvas board game"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/math.combinatorics "0.1.5"]]
  :main ^:skip-aot canvas.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :plugins [[lein-cljfmt "0.7.0"]])
