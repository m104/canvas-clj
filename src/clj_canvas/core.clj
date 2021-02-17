(ns clj-canvas.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clj-canvas.coll :as coll])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (println "Hello, World!"))

(def card-slots [:red :yellow :green :blue :purple])
(def elements [:tone :hue :texture :shape])

(def art-cards
  (->> "canvas-cards.edn"
       io/resource
       slurp
       edn/read-string
       :cards))

(def cards-by-name
  (coll/index-by
   (fn [card]
     (or (:adjective card) (:noun card)))
   art-cards))

(def scoring-conditions
  {; Score if all 5 of the slots have icons.
   ; Bonus icons are also counted as filling slots.
   :composition
   (fn [slots]
     (if (every? (fn [slot-key] (slot-key slots)) card-slots)
       1
       0))
   ; Score if all elements are present at least once
   :variety
   (fn [slots]
     (if nil ; TODO
       1
       0))})

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

(defn scoring-slots
  "Return a list of all possible slot } for scoring"
  [slots]
  (cond (empty? slots) nil
        (= 1 (count slots)) (let [[key values] (first slots)]
                              (for [value values] {key value}))
        :else (let [key (first (keys slots))]
                (for [single (scoring-slots (select-keys slots [key]))
                      rest (scoring-slots (dissoc slots key))]
                  (merge single rest)))))

(defn make-painting
  "Make a new paining from the 3 given cards"
  [cards]
  (and (valid-cards-for-painting? cards)
       (let [combined (apply merge (reverse cards))
             slots (apply merge (map :slots (reverse cards)))]
         {:name (string/join
                 " " [(:noun combined) (:adjective combined)])
          :slots slots
          :scoring-slots (scoring-slots slots)})))
