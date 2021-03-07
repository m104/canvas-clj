(ns clj-canvas.painting-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-canvas.painting :as painting]
            [clj-canvas.data :as data]))

(def fading-card (get data/art-cards-by-name "Fading"))
(def wandering-card (get data/art-cards-by-name "Wandering"))
(def expanse-card (get data/art-cards-by-name "Expanse"))

(def good-set [fading-card wandering-card expanse-card])
(def good-set2 (map #(get data/art-cards-by-name %)
                    ["Divine" "Precious" "Truth"]))

(def good-sets [good-set good-set2])
(def bad-sets [[fading-card fading-card expanse-card]
               [wandering-card expanse-card expanse-card]
               (map #(get data/art-cards-by-name %)
                    ["Masked" "Ominous" "Vast"])
               (map #(get data/art-cards-by-name %)
                    ["Attraction" "Moment" "Purpose"])])

(deftest test-valid-cards-for-painting?
  (testing "good card sets"
    (doseq [input good-sets]
      (is (= true
             (painting/valid-cards-for-painting? input)))))
  (testing "bad card sets"
    (doseq [input bad-sets]
      (is (= false
             (painting/valid-cards-for-painting? input))))))

(deftest test-swatch-combinations
  (testing "simple swatches"
    (let [input {:yellow [:hue]}
          expected [{:yellow :hue}]]
      (is (= expected (painting/swatch-combinations input)))))
  (testing "complex swatches"
    (let [input {:yellow [:hue :tone] :purple [:shape :hue]}
          expected [{:yellow :hue :purple :shape}
                    {:yellow :hue :purple :hue}
                    {:yellow :tone :purple :shape}
                    {:yellow :tone :purple :hue}]]
      (is (= expected (painting/swatch-combinations input))))))

(deftest test-make-painting
  (testing "bad card sets"
    (doseq [input bad-sets]
      (let [painting (apply painting/make-painting input)]
        (is (= clojure.lang.PersistentArrayMap
               (type painting)))
        (is (not (:valid? painting))))))
  (testing "good card sets"
    (doseq [input good-sets]
      (let [painting (apply painting/make-painting input)]
        (is (= clojure.lang.PersistentArrayMap
               (type painting)))
        (is (:valid? painting)))))
  (testing "painting composition"
    (let [painting (apply painting/make-painting good-set)
          expected-swatches {:yellow [:texture :tone]
                             :blue [:hue]
                             :green [:texture]
                             :red [:texture]}]
      (is (= "Fading Expanse" (:name painting)))
      (is (= "Fading Wandering Expanse" (:full-name painting)))
      (is (= good-set (:cards painting)))
      (is (= expected-swatches
             (:swatches painting)))
      (is (= (painting/swatch-combinations expected-swatches)
             (:swatch-combinations painting)))
      (is (= true (:valid? painting))))))

