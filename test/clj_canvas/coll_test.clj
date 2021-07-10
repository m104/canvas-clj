(ns clj-canvas.coll-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-canvas.coll :as coll]))

(deftest test-in?
  (testing "includes"
    (let [input [[5 3 4 1 2 0] 1]
          expected true]
      (is (= expected
             (apply coll/in? input)))))
  (testing "does not include"
    (let [input [[5 4 3 2 0] 1]
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

(deftest test-map-vals
  (testing "map values of a keyval collection"
    (let [input {"a" 1 "b" 2 "c" 3}
          expected {"a" 2 "b" 3 "c" 4}]
      (is (= expected
             (coll/map-vals inc input))))))

(deftest test-pair-combinations
  (testing "empty input"
    (let [set1 #{}
          set2 #{}
          expected []]
      (is (= expected
             (coll/pair-combinations set1 set2)))))
  (testing "half empty input 1"
    (let [set1 #{}
          set2 #{2}
          expected []]
      (is (= expected
             (coll/pair-combinations set1 set2)))))
  (testing "half empty input 2"
    (let [set1 #{1}
          set2 #{}
          expected []]
      (is (= expected
             (coll/pair-combinations set1 set2)))))
  (testing "one pair"
    (let [set1 #{1}
          set2 #{2}
          expected [[[1 2]]]]
      (is (= expected
             (coll/pair-combinations set1 set2)))))
  (testing "one combination 1"
    (let [set1 #{1 2 3}
          set2 #{4}
          expected [[[1 4]] [[3 4]] [[2 4]]]]
      (is (= expected
             (coll/pair-combinations set1 set2)))))
  (testing "one combination 2"
    (let [set1 #{4}
          set2 #{1 2 3}
          expected [[[4 1]] [[4 3]] [[4 2]]]]
      (is (= expected
             (coll/pair-combinations set1 set2)))))
  (testing "simple pairings"
    (let [set1 #{1 2}
          set2 #{3 4}
          expected [[[2 3] [1 4]] [[2 4] [1 3]]]]
      (is (= expected
             (coll/pair-combinations set1 set2)))))

  (testing "complex pairings"
    (let [set1 #{1 2 3}
          set2 #{4 5 6 7}
          expected-count 24
          expected-first [[2 6] [3 4] [1 7]]
          expected-last [[2 4] [3 6] [1 5]]
          result (coll/pair-combinations set1 set2)]
      (is (= expected-count
             (count result)))
      (is (= expected-first (first result)))
      (is (= expected-last (last result))))))
