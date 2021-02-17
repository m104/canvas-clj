(ns clj-canvas.core-test
  (:require [clojure.test :refer :all]
            [clj-canvas.core :as core]))

(def fading-card (get core/cards-by-name "Fading"))
(def wandering-card (get core/cards-by-name "Wandering"))
(def expanse-card (get core/cards-by-name "Expanse"))

(def good-set [fading-card wandering-card expanse-card])
(def good-sets [good-set])
(def bad-sets [[]
               [fading-card]
               [fading-card wandering-card]
               [fading-card expanse-card]
               [fading-card fading-card expanse-card]
               [wandering-card expanse-card expanse-card]])

(core/valid-cards-for-painting? [fading-card wandering-card expanse-card])

(deftest test-valid-cards-for-painting?
  (testing "good card sets"
    (for [input good-sets]
      (is (= true
             (core/valid-cards-for-painting? input)))))
  (testing "bad card sets"
    (for [input bad-sets]
      (is (= false
             (core/valid-cards-for-painting? input))))))

(deftest test-scoring-slots
  (testing "simple slots"
    (let [input {:yellow [:hue]}
          expected [{:yellow :hue}]]
      (is (= expected (core/scoring-slots input)))))
  (testing "complex slots"
    (let [input {:yellow [:hue :tone] :purple [:shape :hue]}
          expected [{:yellow :hue :purple :shape}
                    {:yellow :hue :purple :hue}
                    {:yellow :tone :purple :shape}
                    {:yellow :tone :purple :hue}]]
      (is (= expected (core/scoring-slots input))))))

(deftest test-make-painting
  (testing "good card sets"
    (for [input good-sets]
      (is (= clojure.lang.PersistentArrayMap
             (type (core/make-painting input))))))
  (testing "bad card sets"
    (for [input bad-sets]
      (is (nil? (core/make-painting input)))))
  (testing "painting composition"
    (let [painting (core/make-painting good-set)
          expected-slots {:yellow [:texture :tone]
                          :blue [:hue]
                          :green [:texture]
                          :red [:texture]}]
      (is (= "Expanse Fading" (:name painting)))
      (is (= expected-slots
             (:slots painting)))
      (is (= (core/scoring-slots expected-slots)
             (:scoring-slots painting))))))
