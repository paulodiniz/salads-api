(ns salads.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [cheshire.core :refer :all]
            )
  (:use [markov-chains.core]))

(def ingredients
  [:lettuce
   :rucola
   :cress
   :cole
   :cabbage
   :brocolli
   :tomato
   :white-onion
   :red-onion
   :raw-carrot
   :cooked-carrot
   :raw-beet
   :cooked-beet
   :zucchini
   :eggplant
   :cherry-tomato
   :mango
   :cauliflower
   :onion
   :caper
   :potato
   :egg
   :bacon
   :chicken
   :ham
   :salmon])

(defn row-to-map [row]
  "Converts a row to a map"
  (let [splitted-row (clojure.string/split row #"\s+")
        key (keyword (first splitted-row))
        row-values (map #(read-string %) (drop 1 splitted-row))]
    {[key] (zipmap ingredients row-values)}))

(defn probability-matrix [file]
  "Assembles the probability matrix"
  (->>
   (slurp file)
   (clojure.string/split-lines)
   (map #(row-to-map %))
   (into {})))

(defn my-salad
  []
  (take 5 (generate (probability-matrix "test/salads/bla.txt"))))

(defn my-salad-json
  []
  {:salad (into [] (my-salad))})

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (generate-string (my-salad-json))})

(defn -main []
  (->
   handler
   (jetty/run-jetty {:port 8080})
   (wrap-json-response)
   ))
