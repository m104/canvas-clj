(ns clj-canvas.sandbox
  (:require [clj-canvas.data :as data]
            [clj-canvas.painting :as painting]
            [clj-canvas.scoring :as scoring]
            [clojure.math.combinatorics :as comb])
  (:gen-class))

(def game-scoring-cards
  {:red (get scoring/scoring-cards-by-name "Composition")
   :green  (get scoring/scoring-cards-by-name "Variety")
   :blue (get scoring/scoring-cards-by-name "Repetition")
   :purple (get scoring/scoring-cards-by-name "Style")})

(def first-round (take 5 (shuffle data/art-cards)))

first-round

(def paintings
  (filter :valid? (map (partial apply painting/make-painting)
                       (mapcat comb/permutations
                               (comb/combinations first-round 3)))))

(count paintings)

(take 10
      (sort-by
       #(- (first %))
       (map  #(list
               (scoring/score-all-ribbons
                game-scoring-cards
                (scoring/score-painting game-scoring-cards %))
               (map :name (:cards %)))
             paintings)))