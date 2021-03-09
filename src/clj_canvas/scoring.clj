(ns clj-canvas.scoring
  (:require [clj-canvas.data :as data]
            [clj-canvas.coll :as coll]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.math.numeric-tower :as Math])
  (:gen-class))

(def scoring-fns-by-card-name
  {"Composition" ; Score if all 5 of the swatches have icons
    ; Bonus icons are also counted as filling swatches
   (fn [painting]
     (if (every?
          (fn [swatch-key]
            (swatch-key (:swatches painting)))
          data/swatches)
       1
       0))
   "Consistency" ; Score with exactly 6 visible elements
   (fn [painting]
     (if (= 6
            (count
             (filter #(contains? data/elements %)
                     (flatten (vals (:swatches painting))))))
       1
       0))
   "Emphasis" ; Exactly 1 color hue element
   (fn [painting]
     (if (= 1
            (count
             (filter #(= :hue %)
                     (flatten (vals (:swatches painting))))))
       1
       0))
   "Hierarchy" ; The number of tone elements is greater than or equal to
   ; the number of any other element
   (fn [painting]
     (let [chosen-element :tone
           other-elements (set/difference data/elements #{chosen-element})
           elements (filter #(contains? data/elements %)
                            (flatten (vals (:swatches painting))))
           counts (frequencies elements)
           chosen-count (chosen-element counts 0)
           other-count (apply max (vals (select-keys counts other-elements)))]
       (if (>= chosen-count other-count)
         1
         0)))
   "Movement" ; 3 matching elements in a row
   ; Can be scored multiple times
   ; Each element can only be used in one set
   (fn [painting]
     (let [swatches (:swatches painting)]
       (apply
        +
        (for [element data/elements]
          (if (some true?
                    (for [run (coll/runs-of-n 3 data/swatches)]
                      (every?
                       #(coll/in? element (% swatches))
                       run)))
            1
            0)))))
   "Proportion" ; At least 3 of one element and at least 2 of another element
   (fn [painting]
     (let [elements (filter #(coll/in? % data/elements)
                            (flatten (vals (:swatches painting))))
           element-counts (frequencies elements)
           sorted-counts (vec (sort > (vals element-counts)))]
       (if (and
            (<= 2 (count sorted-counts))
            (<= 3 (nth sorted-counts 0))
            (<= 2 (nth sorted-counts 1)))
         1
         0)))
   "Proximity" ; TODO: Sets of texture and tone elements in adjacent swatches.
   ; Note: each element can only be used in one set
   (fn [painting]
     (let [desired-elements #{:texture :tone}
           runs (coll/runs-of-n 2 data/swatches)]))

   "Repetition" ; Score pairs of 2 shape elements
   (fn [painting]
     (int
      (Math/floor
       (/ (apply max
                 (for [combo (:swatch-combinations painting)]
                   (count (filter #(= :shape %) (vals combo))))) 2))))
   "Space" ; TODO: Hue element and a non-adjacent shape element
   ; Can be scored more than once
   ; Each element can only be counted once per scoring condition
   (fn [_] 0)
   "Style" ; At least 3 texture elements
   (fn [painting]
     (if (<= 3
             (count
              (filter #(= :texture %)
                      (flatten (vals (:swatches painting))))))
       1
       0))
   "Symmetry" ; 2 matching elements in either of the swatch pairs:
   ; (red and purple) or (yellow and blue)
   (fn [painting]
     (let [pairs [[:red :purple] [:yellow :blue]]
           swatches (:swatches painting)
           paired-elements (for [[left right] pairs]
                             [(filter #(contains? data/elements %)
                                      (left swatches))
                              (filter #(contains? data/elements %)
                                      (right swatches))])
           matching-pairs (filter #(not-empty (set/intersection
                                               (set (first %))
                                               (set (last %)))) paired-elements)]
       (if (<= 1
               (count matching-pairs))
         1
         0)))
   "Variety" ; Score if all elements are present at least once
   (fn [painting]
     (if (set/superset?
          (set (flatten (vals (:swatches painting))))
          data/elements)
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

(defn score-bonuses
  [painting]
  (defn occurance-map
    [allow-list coll]
    (frequencies (filter (fn [x] (contains? allow-list x)) coll)))
  (let [icons (flatten (vals (:swatches painting)))
        elements (occurance-map data/elements icons)
        bonuses (occurance-map data/bonuses icons)]
    (apply + (for [[bonus mult] bonuses]
               (* mult (get elements
                            (get data/bonus-map bonus)
                            0))))))

(defn score-painting
  "Returns a hash-map with ribbon color keys and awared ribbon values"
  [scoring-cards-by-ribbon painting]
  (assoc
   (reduce-kv #(assoc %1 %2 ((:scoring %3) painting))
              {}
              scoring-cards-by-ribbon)
   data/bonus-ribbon
   (score-bonuses painting)))

(defn score-ribbons
  "Returns the points awarded for the given scoring card and ribbon count"
  [scoring-card ribbon-count]
  (let [points-for-ribbon-count (into [0] (:points scoring-card))
        points-index (min ribbon-count
                          (dec (count points-for-ribbon-count)))]
    (nth points-for-ribbon-count points-index)))

(defn score-bonus-ribbons
  "Returns the points awarded for the given bonus ribbon count"
  [ribbon-count]
  (* ribbon-count 2))

(defn score-all-ribbons
  "Returns the points awarded for the given ribbon counts and scoring cards"
  [scoring-cards-by-ribbon ribbon-counts]
  (let [bonus-ribbon-count (get ribbon-counts :bonus 0)
        scoring-ribbon-counts (select-keys ribbon-counts data/scoring-ribbons)]
    (+ (score-bonus-ribbons bonus-ribbon-count)
       (apply +
              (for [[ribbon count] scoring-ribbon-counts]
                (score-ribbons (ribbon scoring-cards-by-ribbon)
                               count))))))
