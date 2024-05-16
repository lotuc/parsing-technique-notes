(ns ch4.ch4-01-unger-test
  (:require
   [ch4.ch4-01-unger
    :refer [*epsilon?* gen-partitions match-alpabets match-non-terminal
            some-epsilon-mismatch? some-terminal-mismatch? unger-epsilon
            unger-no-epsilon]]
   [ch4.grammars :refer [grammar-4-fig-4-1 grammar-4-fig-4-3]]
   [hyperfiddle.rcf :as rcf]))

(defn gen-partitions-naive
  "This one captures the essence of marble placing process described in
  the book. Since every cup contains a continuous sequence of
  marbles (marbles are marked with ordered indices), `gen-partitions`
  represents marbles in a cup with [`i`, `l`], the first marble index
  and total marbles in the cup."
  [n-cups n-marbles]
  (let [i0 (if *epsilon?* 0 1)]
    (letfn [(g [cups marbles]
              (let [n-marbles (count marbles)]
                (loop [i i0 r []]
                  ;; i: how many marbles to be put into first cup
                  ;; r: all possible partitions
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

(rcf/tests
 (gen-partitions-naive 2 3)
 := [[[0]   [1 2]]
     [[0 1] [2]]]
 (gen-partitions 2 3)
 := [[[0 1] [1 2]]
     [[0 2] [2 1]]]

 (binding [*epsilon?* true] (gen-partitions-naive 2 3))
 := [[[]      [0 1 2]]
     [[0]     [1 2]]
     [[0 1]   [2]]
     [[0 1 2] []]]
 (binding [*epsilon?* true] (gen-partitions 2 3))
 := [[[0 0] [0 3]]
     [[0 1] [1 2]]
     [[0 2] [2 1]]
     [[0 3] [3 0]]])

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
 (match-non-terminal '{Factor [(\i)]} ['Factor "i"]) := '([:and [\i "i"]])
 (match-non-terminal '{Factor [(\i)]} ['Factor "j"]) := nil)

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
 (unger-no-epsilon
  grammar-4-fig-4-1 "(i+i)*i")
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

(rcf/tests
 (unger-epsilon grammar-4-fig-4-3 "d")
 := '[S [:and
         [L [:and [epsilon ""]]]
         [S [:and [epsilon ""]]]
         [D [:and [\d "d"]]]]]
 (unger-epsilon grammar-4-fig-4-3 "dd")
 := '[S
      [:and
       [L [:and [epsilon ""]]]
       [S [:and
           [L [:and [epsilon ""]]]
           [S [:and [epsilon ""]]]
           [D [:and [\d "d"]]]]]
       [D [:and [\d "d"]]]]])
