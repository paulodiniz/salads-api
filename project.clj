(defproject salads "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [rm-hull/markov-chains "0.1.0"]
                 [cheshire "5.7.1"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.2.1"]]

  :main ^:skip-aot salads.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
