(ns clj-canvas.coll
  (:gen-class))

(defn combinations
  "return all unique n-sized combinations of the items"
  [n items]
  (cond (empty? items) nil
        (= 0 n) nil
        (= 1 n) (for [card items] (list card))
        (= (count items) n) (list items)
        :else (concat
               (for [others (combinations (dec n) (rest items))]
                 (cons (first items) others))
               (combinations n (rest items)))))

(defn index-by
  "Creates a new hashmap with values from coll and keys derived from (f elem)"
  [f coll]
  (apply hash-map
         (mapcat
          (fn [value]
            [(f value) value]) coll)))
