(ns clj-canvas.coll
  (:gen-class))

(defn combinations
  "Return all unique n-sized combinations of the items in the collection"
  [n coll]
  (cond (empty? coll) nil
        (= 0 n) nil
        (= 1 n) (for [card coll] (list card))
        (= (count coll) n) (list coll)
        :else (concat
               (for [others (combinations (dec n) (rest coll))]
                 (cons (first coll) others))
               (combinations n (rest coll)))))

(defn index-by
  "Returns a map with values from coll and keys derived from the given function f"
  [f coll]
  (apply hash-map
         (mapcat
          (fn [value]
            [(f value) value]) coll)))

(defn count-by-values
  "Returns a map where the number of ocurrances of the elements of the given collection
   have been counted"
  [coll]
  (loop [[item & rest] coll
         acc {}]
    (if (not item)
      acc
      (recur rest (assoc acc item (inc (get acc item 0)))))))
