(ns clj-canvas.sandbox
  (:require [clj-canvas.data :as data]
            [clj-canvas.painting :as painting]
            [clj-canvas.scoring :as scoring]
            [clojure.math.combinatorics :as comb])
  (:gen-class))

(def game-scoring-cards
  {:red (get scoring/scoring-cards-by-name "Space")
   :green  (get scoring/scoring-cards-by-name "Hierarchy")
   :blue (get scoring/scoring-cards-by-name "Symmetry")
   :purple (get scoring/scoring-cards-by-name "Style")})

(def first-round (take 20 (shuffle data/art-cards)))
first-round

(def paintings
  (filter :valid? (map (partial apply painting/make-painting)
                       (mapcat comb/permutations
                               (comb/combinations first-round 3)))))
(count paintings)

(take 10
      (sort-by
       #(- (first %))
       (map  (fn [painting]
               (let [ribbons (scoring/score-painting game-scoring-cards painting)]
                 (list
                  (scoring/score-all-ribbons game-scoring-cards ribbons)
                  ribbons
                  (map :name (:cards painting)))))
             paintings)))

(def painting (apply painting/make-painting
                     (map #(get data/art-cards-by-name %)
                          ["Nightmare" "Melancholy" "Peaceful"])))
painting
