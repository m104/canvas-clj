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

(defn index-by
  "Returns a map with values from coll and keys derived from the given function f"
  [f coll]
  (apply hash-map
         (mapcat
          (fn [value]
            [(f value) value]) coll)))
