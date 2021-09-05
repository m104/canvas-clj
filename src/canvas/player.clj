(ns canvas.player
  (:require [canvas.data :as data]
            [canvas.painting :as painting])
  (:gen-class))

(defn make-player
  [id]
  {:id id
   ;:palettes 5
   :cards #{}
   :paintings #{}
   :ribbons {}})

(defn add-art-card
  [player art-card]
  (assert (> data/max-art-cards (count (:cards player)))
          (format "Player cannot hold more than %s art cards"
                  data/max-art-cards))
  (assert (not (contains? (:cards player) art-card))
          "Cannot add duplicate art card to player")
  (update player :cards conj art-card))

(defn remove-art-card
  [player art-card]
  (assert (contains? (:cards player) art-card)
          "Player does not currently hold the art card")
  (update player :cards disj art-card))

(defn create-painting
  [player top middle bottom]
  (assert (> data/max-paintings (count (:paintings player)))
          (format "Player cannot have more than %s paintings"
                  data/max-paintings))
  (let [remove-cards [top middle bottom]
        player (reduce #(remove-art-card %1 %2) player remove-cards)]
    (update player :paintings conj
            (painting/make-painting top middle bottom))))

(defn add-ribbons
  [player ribbon-counts]
  (update player :ribbons
          #(merge-with
            (fn [m1 m2] (+ (or m1 0) (or m2 0)))
            ribbon-counts %)))
