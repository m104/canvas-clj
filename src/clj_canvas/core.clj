(ns clj-canvas.core
  (:require [clj-canvas.data :as data]
            [clj-canvas.painting :as painting]
            [clj-canvas.scoring :as scoring]
            [clj-canvas.player :as player]
            [clojure.set :as set]
            [clojure.string :as string]
            [clojure.math.combinatorics :as comb])
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

(def ribbons
  {:red 3
   :green 2
   :blue 2
   :purple 1
   :bonus 3})
ribbons

(let [bonus-ribbon-count (get ribbons :bonus 0)
      scoring-ribbon-counts (select-keys ribbons data/scoring-ribbons)]
  (println bonus-ribbon-count scoring-ribbon-counts)
  (+ (scoring/score-bonus-ribbons bonus-ribbon-count)
     (apply +
            (for [[ribbon count] scoring-ribbon-counts]
              (scoring/score-ribbons (ribbon game-scoring-cards)
                                     count)))))

(set (flatten (map vals (map :swatches data/art-cards))))

(time (count (filter painting/valid-cards-for-painting? (comb/combinations data/art-cards 3))))
(time (count (comb/combinations data/art-cards 3)))
