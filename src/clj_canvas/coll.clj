(ns clj-canvas.coll
  (:gen-class))

(defn in?
  "Returns true if coll contains elm"
  [elm coll]
  (boolean (some #(= elm %) coll)))

(defn runs-of-n
  "Returns all sequential runs of n elements in the collection"
  [n coll]
  (loop [remaining (count coll)
         coll coll
         runs []]
    (if (< remaining n)
      runs
      (recur (dec remaining)
             (rest coll)
             (conj runs (take n coll))))))

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
