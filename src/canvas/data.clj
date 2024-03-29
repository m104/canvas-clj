(ns canvas.data
  (:require [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.java.io :as io]
            [canvas.coll :as coll]
            [canvas.data :as data])
  (:gen-class))

(def max-art-cards 5)
(def max-paintings 3)
(def scoring-ribbons [:red :green :blue :purple])
(def bonus-ribbon :bonus)
(def ribbons (conj scoring-ribbons bonus-ribbon))
(def swatches [:red :yellow :green :blue :purple])
(def adjacent-swatches-map
  (let [base-pairs (coll/runs-of-n 2 swatches)
        all-pairs (mapcat #(list % (reverse %))
                          base-pairs)]
    (coll/map-vals #(set (map last %))
                   (group-by first
                             all-pairs))))
(def bonus-map {:bonus-tone :tone
                :bonus-hue :hue
                :bonus-texture :texture
                :bonus-shape :shape})
(def bonuses (set (keys bonus-map)))
(def elements (set (vals bonus-map)))
(def icons (set/union elements bonuses))

(def art-cards
  (for [card (->> "art-cards.edn"
                  io/resource
                  slurp
                  edn/read-string
                  :cards)]
    (assoc card
           :name
           (or (:adjective card)
               (:noun card)))))

(def art-cards-by-name
  (coll/index-by :name art-cards))
