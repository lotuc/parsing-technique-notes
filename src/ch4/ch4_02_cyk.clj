(ns ch4.ch4-02-cyk
  (:require
   [ch4.grammars :refer [grammar-fig-4-15 grammar-fig-4-6 grammar-fig-4-10 epsilon?]]))

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

(defn eliminate-epsilon [{:keys [rules] :as g}]
  (letfn [(epsilon1? [right]
            (and (= 1 (count right)) (epsilon? (first right))))
          (on-alpha-A-beta* [A A' seq-rule]
            (let [[alpha A-beta] (split-with #(not= A %) seq-rule)]
              (if (empty? A-beta)
                [seq-rule]
                (let [[_ & beta] A-beta]
                  (->> [(concat alpha beta)
                        (concat alpha [A'] beta)]
                       (mapcat (partial on-alpha-A-beta* A A'))
                       (map (fn [v] (if (empty? v) '(epsilon) v))))))))
          (on-alpha-A-beta [[A A'] rules]
            (reduce-kv
             (fn [r left rights]
               (->> rights
                    (mapcat (partial on-alpha-A-beta* A A'))
                    (reduce (fn [r v] (cond-> r (not-any? #{v} r) (conj v))) [])
                    (assoc r left)))
             {}
             rules))
          (on-A-alpha [[A A'] rules]
            (if-some [rs (seq (filter (complement epsilon1?) (rules A)))]
              (assoc rules A' (into [] rs))
              (reduce-kv
               (fn [r left rights]
                 (if-some [rs (seq (filter (fn [v] (not-any? #{A'} v)) rights))]
                   (assoc r left (into [] rs))
                   r))
               {}
               rules)))
          (find-epsilon-lefts [handled rules]
            ;; find A for rule A -> (epsilon)
            (reduce-kv
             (fn [r left rights]
               (cond-> r
                 (and (not (handled left)) (some epsilon1? rights)) (conj left)))
             #{}
             rules))
          (elimiate-non-terminal [rules AA']
            (->> rules
                 (on-alpha-A-beta AA')
                 (on-A-alpha AA')))
          (eliminate-epsilon* [handled rules]
            (let [h (->> (find-epsilon-lefts handled rules)
                         (map (fn [A] [A (symbol (str (name A) "'"))])))]
              [(mapcat identity h) (reduce elimiate-non-terminal rules h)]))]
    (loop [r rules
           ;; handled non-terminal A and converted A'
           handled #{}]
      (let [[h r'] (eliminate-epsilon* handled r)]
        (if (= r r')
          (assoc g :rules r)
          (recur r' (reduce conj handled h)))))))

(comment
  (eliminate-epsilon grammar-fig-4-10)
  (eliminate-epsilon grammar-fig-4-6))
