(ns clj-canvas.core
  (:require [clj-canvas.data :as data]
            [clj-canvas.coll :as coll]
            [clj-canvas.painting :as painting]
            [clj-canvas.scoring :as scoring]
            [clj-canvas.player :as player]
            [clojure.set :as set]
            [clojure.string :as string])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (println "Hello, World!"))

(def painting1
  (apply painting/make-painting
         (map (fn [name] (get data/art-cards-by-name name))
              ["Wandering" "Fading" "Truth"])))

(def painting2
  (apply painting/make-painting
         (map (fn [name] (get data/art-cards-by-name name))
              ["Heightened" "Peaceful" "Trap"])))

(def painting3
  (apply painting/make-painting
         (map (fn [name] (get data/art-cards-by-name name))
              ["Wandering" "Divine" "Expanse"])))

painting1
painting2
painting3

(for [painting [painting1 painting2 painting3]
      scoring-card scoring/scoring-cards]
  [(string/join " : " [(:name painting) (:name scoring-card)])
   ((:scoring scoring-card) painting)])

(def game-scoring-cards
  {:red (get scoring/scoring-cards-by-name "Composition")
   :green  (get scoring/scoring-cards-by-name "Variety")
   :blue (get scoring/scoring-cards-by-name "Repetition")
   :purple (get scoring/scoring-cards-by-name "Style")})

(scoring/score-painting game-scoring-cards painting3)

(let [[first second] (sort > (vals {:hue 2 :shape 1 :tone 4}))]
  (println first)
  (println second)
  [first second])

(sort > (vals {:hue 2 :shape 1 :tone 4}))
(nth (vec '(4 2 1)) 1)