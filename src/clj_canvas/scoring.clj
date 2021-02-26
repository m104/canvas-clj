(ns clj-canvas.scoring
  (:require [clj-canvas.data :as data]
            [clj-canvas.coll :as coll]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.math.numeric-tower :as Math])
  (:gen-class))


(def scoring-fns-by-card-name
  {"Composition" ; Score if all 5 of the slots have icons.
    ; Bonus icons are also counted as filling slots.
   (fn [painting]
     (if (every?
          (fn [slot-key]
            (slot-key (:slots painting)))
          data/slots)
       1
       0))
   "Variety" ; Score if all elements are present at least once
   (fn [painting]
     (if (set/superset?
          (set (flatten (vals (:slots painting))))
          data/elements)
       1
       0))
   "Repetition" ; Score pairs of 2 shape elements
   (fn [painting]
     (int
      (Math/floor
       (/ (apply max
                 (for [combo (:slot-combinations painting)]
                   (count (filter #(= :shape %) (vals combo))))) 2))))
   "Consistency" ; Score with exactly 6 visible elements
   (fn [painting]
     (if (= 6
            (count
             (filter #(contains? data/elements %)
                     (flatten (vals (:slots painting))))))
       1
       0))})

(def scoring-cards
  (for [card (->> "scoring-cards.edn"
                  io/resource
                  slurp
                  edn/read-string
                  :cards)]
    (assoc card
           :scoring
           (get scoring-fns-by-card-name (:name card)))))

; Ensure that all scoring cards have a scoring function
(assert (every? :scoring scoring-cards))

(def scoring-cards-by-name
  (coll/index-by :name scoring-cards))

(defn score-elements
  [scoring-fn painting]
  0)

(defn score-bonuses
  [painting]
  (defn occurance-map
    [allow-list coll]
    (coll/count-by-values (filter (fn [x] (contains? allow-list x)) coll)))
  (let [icons (flatten (vals (:slots painting)))
        elements (occurance-map data/elements icons)
        bonuses (occurance-map data/bonuses icons)]
    (reduce + (for [[bonus mult] bonuses]
                (* mult (get elements
                             (get 'data/bonus-map bonus)
                             0))))))
