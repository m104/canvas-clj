(ns clj-canvas.core
  (:require [clj-canvas.data :as data]
            [clj-canvas.coll :as coll]
            [clj-canvas.painting :as painting]
            [clj-canvas.scoring :as scoring]
            [clojure.set :as set]
            [clojure.string :as string])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (println "Hello, World!"))

(def painting1
  (painting/make-painting
   (map (fn [name] (get data/art-cards-by-name name))
        ["Wandering" "Fading" "Truth"])))

(def painting2
  (painting/make-painting
   (map (fn [name] (get data/art-cards-by-name name))
        ["Heightened" "Peaceful" "Trap"])))

(def painting3
  (painting/make-painting
   (map (fn [name] (get data/art-cards-by-name name))
        ["Wandering" "Divine" "Expanse"])))

painting1
painting2
painting3

(:swatches (painting/make-painting
            (map (fn [name] (get data/art-cards-by-name name))
                 ["Peaceful" "Perspective" "Purpose"])))

(for [painting [painting1 painting2 painting3]
      scoring-card scoring/scoring-cards]
  [(string/join " : " [(:name painting) (:name scoring-card)])
   ((:scoring scoring-card) painting)])