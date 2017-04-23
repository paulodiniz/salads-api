(ns salads.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [cheshire.core :refer :all]
            [environ.core :refer [env]])
  (:use [markov-chains.core])
  (:gen-class))

(def salad-file
  (->
   "test/salads/bla.txt"
   (slurp)
   (clojure.string/split-lines)))

(def ingredients
  (->>
   salad-file
   (map #(clojure.string/split % #"\s+"))
   (map #(first %))
   (map #(keyword %))
   (vec)))

(defn row-to-map [row]
  "Converts a row to a map"
  (let [splitted-row (clojure.string/split row #"\s+")
        key (keyword (first splitted-row))
        row-values (map #(read-string %) (drop 1 splitted-row))]
    {[key] (zipmap ingredients row-values)}))

(defn probability-matrix []
  "Assembles the probability matrix"
  (->>
   salad-file
   (map #(row-to-map %))
   (into {})))

(defn my-salad []
  (->>
   (probability-matrix)
   (generate)
   (take 5)))

(defn my-salad-json []
  {:salad (into [] (my-salad))})

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (generate-string (my-salad-json))})

(def app (wrap-json-response handler))

(defn -main []
  (let [port (Integer. (or (env :port) 5000))]
    (jetty/run-jetty app {:port port})))
