(ns ch4.ch4-01-unger
  (:require
   [ch4.grammars :refer [grammar-4-1-1 grammar-4-fig-4-1 terminal?]]))

(defn gen-partitions
  [cups marbles]
  (letfn [(g [cups marbles]
            (let [n (count marbles)]
              (loop [i 1 r []]
                (if (<= i n)
                  (let [r' (let [[fcup & rcups] cups]
                             (if (seq rcups)
                               (let [fcup' (into fcup (take i marbles))
                                     rmarbles (drop i marbles)]
                                 (->> (g rcups rmarbles)
                                      (map (fn [v] (into [fcup'] v)))
                                      (into r)))
                               [[(into fcup marbles)]]))]
                    (recur (inc i) r'))
                  r))))]
    (when-not (empty? cups)
      (g (map (constantly []) cups) marbles))))

(defn match-rule-right
  "Build a and tree (hiccup form)"
  [rule-right input]
  (->> (gen-partitions rule-right input)
       (mapv (fn [parts] (into [:and] (map (fn [k p] [k p]) rule-right parts))))))

(comment
  (let [{:keys [start rules]} grammar-4-1-1]
    (for [r (start rules)]
      (match-rule-right r "pqrs"))))

(defn reject-on-terminal-mismatch [[_and & leaves]]
  (some (fn [[alphabet matched]]
          (when (terminal? alphabet)
            (not= [alphabet] matched)))
        leaves))

(defn match-rule-left [grammar [rule-left input]]
  (->> (for [r (get-in grammar [:rules rule-left])]
         (match-rule-right r input))
       (mapcat identity)
       (filter (complement reject-on-terminal-mismatch))
       seq))

(defn- unger-no-epsilon-topdown
  [grammar [alphabet input]]
  (if (terminal? alphabet)
    [alphabet input]
    (when-some [ors (seq (for [i (match-rule-left grammar [alphabet input])
                               :let [i-ands (map (partial unger-no-epsilon-topdown grammar)
                                                 (rest i))]
                               :when (every? identity i-ands)]
                           (into [:and] i-ands)))]
      (if (= 1 (count ors))
        [alphabet (first ors)]
        [alphabet (into [:or] ors)]))))

(defn unger-no-epsilon [grammar input]
  (letfn []
    (unger-no-epsilon-topdown grammar [(:start grammar) input])))

(comment
  (unger-no-epsilon grammar-4-fig-4-1 "i")
  (unger-no-epsilon grammar-4-fig-4-1 "(i)")
  (unger-no-epsilon grammar-4-fig-4-1 "(i+i)")
  (unger-no-epsilon grammar-4-fig-4-1 "(i+i)*i"))
