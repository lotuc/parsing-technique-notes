(ns ch4.ch4-02-cyk
  (:require
   [ch4.grammars :refer [grammar-fig-4-15 grammar-fig-4-6]]))

(defn R-epsilon [{:keys [rules] :as g}]
  (letfn [(f [r-epsilon]
            (reduce-kv
             (fn [r-epsilon' left rights]
               (cond-> r-epsilon'
                 (some (fn [right] (every? r-epsilon right)) rights)
                 (conj left)))
             r-epsilon
             rules))]
    (loop [r #{'Empty}]
      (let [r' (f r)]
        ;; closure algorithm: repeat until no new added
        (if (= r r') r (recur r'))))))

(comment
  (R-epsilon grammar-fig-4-6))

(defn inverse-CNF [{:keys [rules]}]
  (reduce-kv
   (fn [r left rights]
     (reduce
      (fn [r right]
        (update r right (fnil conj #{}) left))
      r
      rights))
   {}
   rules))

(comment
  (inverse-CNF grammar-fig-4-15))

(defn recognition-table-CNF [{:keys [rules] :as g} s]
  (let [n (count s)
        inversed-rules (inverse-CNF g)
        table (make-array Object n n)]
    (doseq [i (range n)
            :let [v (get inversed-rules (list (nth s i)))]]
      (aset table i 0 v))
    (doseq [j (range 1 n)]
      (doseq [i (range 0 (- n j))]
        (->> (for [hl (range j)
                   :let [head (aget table i hl)
                         tail (aget table (+ i hl 1) (- j hl 1))]]
               (->> (for [vh head
                          vt tail]
                      (get inversed-rules [vh vt]))
                    (mapcat identity)))
             (mapcat identity)
             set
             (aset table i j))))
    table))

(comment
  (def t (recognition-table-CNF grammar-fig-4-15 "123"))
  (aget t 0 2)
  (def t1 (recognition-table-CNF grammar-fig-4-15 "32.5e+1"))
  (aget t1 0 (dec (count "32.5e+1"))))
