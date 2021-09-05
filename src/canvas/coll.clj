(ns canvas.coll
  (:gen-class))

(defn in?
  "Returns true if coll contains elm"
  [coll elm]
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

(defn pair-combinations
  "Returns all unique combinations of valid pairs, derived from coll1 and coll2"
  [[elem1 & rest1 :as coll1]
   [elem2 & rest2 :as coll2]]
  (cond (or (nil? elem1)
            (nil? elem2)) '()
        ; 1 x n
        (empty? rest1) (for [elem2 coll2]
                         [[elem1 elem2]])
        ; n x 1
        (empty? rest2) (for [elem1 coll1]
                         [[elem1 elem2]])
        :else (for [elem2 coll2
                    combo (pair-combinations rest1 (disj coll2 elem2))]
                (into combo [[elem1 elem2]]))))

(defn index-by
  "Returns a map with values from coll and keys derived from the given function f"
  [f coll]
  (apply hash-map
         (mapcat
          (fn [value]
            [(f value) value]) coll)))

(defn map-vals
  "Returns a map with identical keys to the given map but with the respective
   values mapped with function f"
  [f map]
  (into {} (for [[k v] map] [k (f v)])))
