(ns ch4.ch4-01-unger
  (:require
   [ch4.grammars :refer [epsilon? grammar-4-fig-4-1 terminal-eq? terminal?]]
   [hyperfiddle.rcf :as rcf]))

(defn gen-partitions
  [n-cups n-marbles & {:keys [allow-empty?]}]
  (let [i0 (if allow-empty? 0 1)]
    (letfn [(g [cups marbles]
              (let [n-marbles (count marbles)]
                (loop [i i0 r []]
                  ;; i: how many marbles to be put into first cup
                  ;; r: all possible pa
                  (if (<= i n-marbles)
                    (let [r' (let [[cup0 & cups'] cups]
                               (if cups'
                                 (let [cup0' (into cup0 (take i marbles))
                                       marbles' (drop i marbles)]
                                   (->> (g cups' marbles')
                                        (map (fn [v] (into [cup0'] v)))
                                        (into r)))
                                 [[(into cup0 marbles)]]))]
                      (recur (inc i) r'))
                    r))))]
      (g (map (constantly []) (range n-cups))
         (range n-marbles)))))

(defn match-alpabets
  "Build a and tree (hiccup form)"
  [alpabets input & {:keys [sequence-subs]}]
  (let [input-subs (cond sequence-subs sequence-subs
                         (string? input)
                         (fn [indexes]
                           (when (seq indexes)
                             (subs input
                                   (first indexes)
                                   (inc (last indexes)))))
                         :else
                         (fn [indexes]
                           (map #(nth input %) indexes)))
        nth-alpabet #(nth alpabets %)]
    (letfn [(match-partition [p]
              (->> (map-indexed
                    (fn [rule-idx input-indexes]
                      [(nth-alpabet rule-idx)
                       (input-subs input-indexes)])
                    p)
                   (into [:and])))]
      (map match-partition
           (gen-partitions (count alpabets) (count input))))))

(defn some-terminal-mismatch? [[combinator & pairs]]
  (and (= combinator :and)
       (some (fn [[grammar-alpabet matched]]
               (and (terminal? grammar-alpabet)
                    (not (terminal-eq? grammar-alpabet matched))))
             pairs)))

(defn some-epsilon-mismatch? [[combinator & pairs]]
  (and (= combinator :and)
       (some (fn [[grammar-alpabet matched]]
               (and (epsilon? grammar-alpabet)
                    (seq matched)))
             pairs)))

(defn match-non-terminal [grammar [non-terminal input] & {:keys [epsilon?]}]
  (let [accept? (if epsilon?
                  (fn [m]
                    (and (not (some-terminal-mismatch? m))
                         (not (some-epsilon-mismatch? m))))
                  (complement some-terminal-mismatch?))]
    (->> (for [r (get-in grammar [:rules non-terminal])]
           (match-alpabets r input))
         (mapcat identity)
         (filter accept?)
         seq)))

(defn- unger-no-epsilon-topdown
  [grammar [alphabet input]]
  (if (terminal? alphabet)
    [alphabet input]
    (when-some [ors (seq (for [[_ & pairs] (match-non-terminal grammar [alphabet input])
                               :let [r (map (partial unger-no-epsilon-topdown grammar)
                                            pairs)]
                               :when (every? some? r)]
                           (into [:and] r)))]
      (if (= 1 (count ors))
        [alphabet (first ors)]
        [alphabet (into [:or] ors)]))))

(defn unger-no-epsilon [grammar input]
  (letfn []
    (unger-no-epsilon-topdown grammar [(:start grammar) input])))

(rcf/tests
 (gen-partitions 2 3)
 := [[[0] [1 2]]
     [[0 1] [2]]]

 (gen-partitions 2 3 :allow-empty? true)
 := [[[] [0 1 2]]
     [[0] [1 2]]
     [[0 1] [2]]
     [[0 1 2] []]])

(rcf/tests
 (match-alpabets '(A B C) "pqrs")
 := '([:and [A "p"] [B "q"] [C "rs"]]
      [:and [A "p"] [B "qr"] [C "s"]]
      [:and [A "pq"] [B "r"] [C "s"]]))

(rcf/tests
 (boolean (some-terminal-mismatch? '[:and [A "p"] [B "q"]]))  := false
 (boolean (some-terminal-mismatch? '[:and [A "p"] [\q "q"]])) := false
 (boolean (some-terminal-mismatch? '[:and [A "p"] [\a "q"]])) := true

 (boolean (some-epsilon-mismatch? '[:and [A "p"] [epsilon ""]]))  := false
 (boolean (some-epsilon-mismatch? '[:and [A "p"] [epsilon "a"]])) := true)

(rcf/tests
 (match-non-terminal '{:rules {Factor [(\i)]}} ['Factor "i"]) := '([:and [\i "i"]])
 (match-non-terminal '{:rules {Factor [(\i)]}} ['Factor "j"]) := nil)

(rcf/tests
 (unger-no-epsilon grammar-4-fig-4-1 "i")
 := '[Expr [:and [Term [:and [Factor [:and [\i "i"]]]]]]]
 (unger-no-epsilon grammar-4-fig-4-1 "(i)")
 := '[Expr
      [:and
       [Term
        [:and
         [Factor
          [:and
           [\( "("]
           [Expr [:and [Term [:and [Factor [:and [\i "i"]]]]]]]
           [\) ")"]]]]]]]
 (unger-no-epsilon grammar-4-fig-4-1 "(i+i)")
 := '[Expr
      [:and
       [Term
        [:and
         [Factor
          [:and
           [\( "("]
           [Expr
            [:and
             [Expr [:and [Term [:and [Factor [:and [\i "i"]]]]]]]
             [\+ "+"]
             [Term [:and [Factor [:and [\i "i"]]]]]]]
           [\) ")"]]]]]]]
 (unger-no-epsilon grammar-4-fig-4-1 "(i+i)*i")
 := '[Expr
      [:and
       [Term
        [:and
         [Term
          [:and
           [Factor
            [:and
             [\( "("]
             [Expr
              [:and
               [Expr [:and [Term [:and [Factor [:and [\i "i"]]]]]]]
               [\+ "+"]
               [Term [:and [Factor [:and [\i "i"]]]]]]]
             [\) ")"]]]]]
         [\* "*"]
         [Factor [:and [\i "i"]]]]]]])
