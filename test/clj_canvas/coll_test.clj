(ns clj-canvas.coll-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-canvas.coll :as coll]))

(deftest test-in?
  (testing "includes"
    (let [input [1 [5 3 4 1 2 0]]
          expected true]
      (is (= expected
             (apply coll/in? input)))))
  (testing "does not include"
    (let [input [1 [5 4 3 2 0]]
          expected false]
      (is (= expected
             (apply coll/in? input))))))

(deftest test-runs-of-n
  (testing "array of values"
    (let [input [3 [0 1 2 3 4 5 6 7]]
          expected [[0 1 2]
                    [1 2 3]
                    [2 3 4]
                    [3 4 5]
                    [4 5 6]
                    [5 6 7]]]
      (is (= expected
             (apply coll/runs-of-n input))))))

(deftest test-index-by
  (testing "indexable collection"
    (let [input [75 76 77]
          expected {"K" 75 "L" 76 "M" 77}]
      (is (= expected
             (coll/index-by
              (fn [value] (str (char value)))
              input))))))

(deftest test-combinations
  (testing "one item, pick one"
    (let [input [1 [1]]
          expected [[1]]]
      (is (= expected
             (apply coll/combinations input)))))
  (testing "three items, pick two"
    (let [input [2 [1 2 3]]
          expected [[1 2] [1 3] [2 3]]]
      (is (= expected
             (apply coll/combinations input)))))
  (testing "count of 20 items, pick 3"
    (let [input [3 (range 1 21)]
          expected 1140]
      (is (= expected
             (count
              (apply coll/combinations input)))))))
