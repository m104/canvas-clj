(ns clj-canvas.painting
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

(defn slot-combinations
  "Return a list of all possible slot combinations for scoring"
  [slots]
  (cond (empty? slots) nil
        (= 1 (count slots)) (let [[key values] (first slots)]
                              (for [value values] {key value}))
        :else (let [key (first (keys slots))]
                (for [single (slot-combinations (select-keys slots [key]))
                      rest (slot-combinations (dissoc slots key))]
                  (merge single rest)))))

(defn make-painting
  "Make a new paining from the 3 given cards"
  [cards]
  (let [rev-cards (reverse cards)
        combined (apply merge rev-cards)
        slots (apply merge (map :slots rev-cards))]
    {:name (string/join
            " " [(:adjective combined) (:noun combined)])
     :full-name (string/join
                 " " (map #(or (:noun %) (:adjective %)) cards))
     :cards cards
     :slots slots
     :slot-combinations (slot-combinations slots)
     :valid? (valid-cards-for-painting? cards)}))
