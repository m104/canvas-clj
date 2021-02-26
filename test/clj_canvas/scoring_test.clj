(ns clj-canvas.scoring-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-canvas.painting :as painting]
            [clj-canvas.scoring :as scoring]
            [clj-canvas.data :as data]))

(defn run-score
  [art-card-names scoring-card-name]
  (let [art-cards (for [name art-card-names]
                    (get data/art-cards-by-name name))
        painting (painting/make-painting art-cards)
        scoring-fn (get scoring/scoring-fns-by-card-name scoring-card-name)]
    (scoring-fn painting)))

(deftest test-scoring-conditions
  (testing "Variety"
    (is (= 1 (run-score ["Wandering" "Fading" "Truth"] "Variety")))
    (is (= 0 (run-score ["Divine" "Precious" "Truth"] "Variety"))))
  (testing "Composition"
    (is (= 1 (run-score ["Wandering" "Fading" "Truth"] "Composition")))
    (is (= 0 (run-score ["Divine" "Precious" "Truth"] "Composition"))))
  (testing "Repetition"
    (is (= 1 (run-score ["Divine" "Precious" "Truth"] "Repetition")))
    (is (= 0 (run-score ["Wandering" "Fading" "Truth"] "Repetition"))))
  (testing "Consistency"
    (is (= 1 (run-score ["Wandering" "Fading" "Truth"] "Consistency")))
    (is (= 0 (run-score ["Divine" "Precious" "Truth"] "Consistency")))))

