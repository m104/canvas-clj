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

(def painting
  (painting/make-painting
   (map (fn [name] (get data/art-cards-by-name name))
        ["Wandering" "Fading" "Truth"])))

(def painting2
  (painting/make-painting
   (map (fn [name] (get data/art-cards-by-name name))
        ["Improbable" "Liberated" "Complexity"])))

(def painting3
  (painting/make-painting
   (map (fn [name] (get data/art-cards-by-name name))
        ["Wandering" "Divine" "Expanse"])))

painting
painting2
painting3

(let [pairs [[:red :purple] [:yellow :blue]]
      swatches (:swatches painting3)
      paired-elements (for [[left right] pairs]
                        [(filter #(contains? data/elements %)
                                 (left swatches))
                         (filter #(contains? data/elements %)
                                 (right swatches))])
      matching-pairs (filter #(not-empty (set/intersection
                                          (set (first %))
                                          (set (last %)))) paired-elements)]
  matching-pairs
  )


(for [painting [painting painting2 painting3]
      scoring-card scoring/scoring-cards]
  [(string/join " : " [(:name painting) (:name scoring-card)])
   ((:scoring scoring-card) painting)])