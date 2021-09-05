(ns canvas.painting
  (:require [clojure.string :as string])
  (:gen-class))

(defn valid-cards-for-painting?
  [cards]
  (boolean
   (and (= 3 (count cards))
        (apply distinct? cards)
        (let [adjectives (count (filter :adjective cards))
              nouns (count (filter :noun cards))]
          (and (<= 1 adjectives)
               (>= 2 adjectives)
               (<= 1 nouns)
               (>= 2 nouns))))))

(defn swatch-combinations
  "Return a list of all possible swatch combinations for scoring"
  [swatches]
  (cond (empty? swatches) nil
        (= 1 (count swatches)) (let [[key values] (first swatches)]
                                 (for [value values] {key value}))
        :else (let [key (first (keys swatches))]
                (for [single (swatch-combinations (select-keys swatches [key]))
                      rest (swatch-combinations (dissoc swatches key))]
                  (merge single rest)))))

(defn make-painting
  "Make a new paining from the given cards"
  [top middle bottom]
  (let [cards [top middle bottom]
        rev-cards (reverse cards)
        combined (apply merge rev-cards)
        swatches (apply merge (map :swatches rev-cards))]
    {:name (string/join
            " " [(:adjective combined) (:noun combined)])
     :full-name (string/join
                 " " (map #(or (:noun %) (:adjective %)) cards))
     :cards cards
     :swatches swatches
     :valid? (valid-cards-for-painting? cards)}))
