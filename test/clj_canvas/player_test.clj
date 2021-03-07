(ns clj-canvas.player-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-canvas.data :as data]
            [clj-canvas.painting :as painting]
            [clj-canvas.player :as player])
  (:gen-class))

(def base-player (player/make-player "1"))
(def wandering-card (get data/art-cards-by-name "Wandering"))
(def mess-card (get data/art-cards-by-name "Mess"))
(def truth-card (get data/art-cards-by-name "Truth"))

(defn player-with-cards
  [cards]
  (reduce #(player/add-art-card %1 %2) base-player cards))

(deftest test-add-art-card
  (testing "with no art cards"
    (let [player (player/add-art-card base-player wandering-card)]
      (is (= clojure.lang.PersistentArrayMap
             (type player)))
      (is (= #{wandering-card} (:cards player)))))
  (testing "with 2 art cards"
    (let [held-cards [wandering-card mess-card]
          player (player-with-cards held-cards)
          updated-player (player/add-art-card player truth-card)]
      (is (= clojure.lang.PersistentArrayMap
             (type updated-player)))
      (is (= #{wandering-card mess-card truth-card} (:cards updated-player)))))
  (testing "with max art cards"
    (let [held-cards (take data/max-art-cards data/art-cards)
          player (player-with-cards held-cards)
          additional-card (nth data/art-cards data/max-art-cards)]
      (is (thrown? AssertionError
                   (player/add-art-card player additional-card)))))
  (testing "adding duplicate card"
    (let [held-cards [wandering-card truth-card]
          player (player-with-cards held-cards)]
      (is (thrown? AssertionError
                   (player/add-art-card player truth-card))))))

(deftest test-remove-art-card
  (testing "with no cards held"
    (let [player base-player
          card truth-card]
      (is (thrown? AssertionError
                   (player/remove-art-card player card)))))
  (testing "without the card held"
    (let [held-cards [wandering-card]
          player (player-with-cards held-cards)
          card truth-card]
      (is (thrown? AssertionError
                   (player/remove-art-card player card)))))
  (testing "with the card held"
    (let [held-cards [wandering-card truth-card]
          player (player-with-cards held-cards)
          card truth-card
          updated-player (player/remove-art-card player card)]
      (is (= #{wandering-card} (:cards updated-player))))))

(deftest test-create-painting
  (testing "with cards not currently held"
    (let [held-cards [wandering-card truth-card]
          player (player-with-cards held-cards)
          chosen-cards [wandering-card truth-card mess-card]]
      (is (thrown? AssertionError
                   (apply player/create-painting player chosen-cards)))))
  (testing "with all cards currently held"
    (let [held-cards [wandering-card truth-card mess-card]
          player (player-with-cards held-cards)
          chosen-cards [wandering-card truth-card mess-card]
          updated-player (apply player/create-painting player chosen-cards)
          created-painting (first (:paintings updated-player))
          expected-painting (apply painting/make-painting chosen-cards)]
      (is (= 1 (count (:paintings updated-player))))
      (is (= expected-painting created-painting))
      (is (= #{} (:cards updated-player))))))

(deftest test-add-ribbons
  (testing "no ribbons awarded"
    (let [ribbons {:blue 1 :bonus 2}
          updated-player (player/add-ribbons base-player ribbons)]
      (is (= ribbons (:ribbons updated-player)))))
  (testing "non-overlapping ribbons awarded"
    (let [base-ribbons {:blue 1 :bonus 2}
          player (player/add-ribbons base-player base-ribbons)
          ribbons {:red 1 :green 2}
          updated-player (player/add-ribbons player ribbons)
          expected-ribbons {:blue 1 :red 1 :green 2 :bonus 2}]
      (is (= expected-ribbons (:ribbons updated-player)))))
  (testing "overlapping ribbons awarded"
    (let [base-ribbons {:blue 1 :bonus 2}
          player (player/add-ribbons base-player base-ribbons)
          ribbons {:blue 1 :green 2 :bonus 1}
          updated-player (player/add-ribbons player ribbons)
          expected-ribbons {:blue 2 :green 2 :bonus 3}]
      (is (= expected-ribbons (:ribbons updated-player))))))