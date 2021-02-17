(ns clj-canvas.coll-test
  (:require [clojure.test :refer :all]
            [clj-canvas.coll :as coll]))

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
