(ns ch4.ch4-01-unger
  (:require
   [ch4.grammars :refer [epsilon? grammar-4-fig-4-3 terminal-eq? terminal?]]))

(def ^:dynamic *epsilon?* false)

(defn gen-partitions
  "every partition is a sequence of [marble-idx num-of-marbles]."
  [n-cups n-marbles]
  (let [i0 (if *epsilon?* 0 1)]
    (letfn [(add-to-cup [cup marble0-idx i]
              (if cup
                (update cup 1 + i)
                [marble0-idx i]))
            (g [cups marble0-idx n-marbles]
              (loop [i i0 r []]
                ;; i: how many marbles to be put into first cup
                ;; r: all possible partitions
                (if (<= i n-marbles)
                  (let [r' (let [[cup0 & cups'] cups]
                             (if cups'
                               (let [cup0' (add-to-cup cup0 marble0-idx i)]
                                 (->> (g cups' (+ marble0-idx i) (- n-marbles i))
                                      (map (fn [v] (into [cup0'] v)))
                                      (into r)))
                               [[(add-to-cup cup0 marble0-idx n-marbles)]]))]
                    (recur (inc i) r'))
                  r)))]
      (g (map (constantly nil) (range n-cups)) 0 n-marbles))))

(defn- input-subs [input [i l]]
  (assert (string? input))
  (subs input i (+ i l)))

(defn match-alpabets
  "Build a and tree (hiccup form)"
  [alpabets input]
  (let [nth-alpabet #(nth alpabets %)]
    (letfn [(match-partition [p]
              (->> (map-indexed
                    (fn [rule-idx i-l]
                      [(nth-alpabet rule-idx)
                       (input-subs input i-l)])
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

(defn match-non-terminal [rules [non-terminal input]]
  (let [accept? (if epsilon?
                  (fn [m]
                    (and (not (some-terminal-mismatch? m))
                         (not (some-epsilon-mismatch? m))))
                  (complement some-terminal-mismatch?))]
    (->> (for [r (rules non-terminal)]
           (match-alpabets r input))
         (mapcat identity)
         (filter accept?)
         seq)))

(defn unger-no-epsilon
  [{:keys [start rules] :as grammar} input]
  (letfn [(topdown [[alphabet input]]
            (if (terminal? alphabet)
              [alphabet input]
              (when-some [ors (seq (for [[_ & pairs] (match-non-terminal rules [alphabet input])
                                         :let [r (map topdown pairs)]
                                         :when (every? some? r)]
                                     (into [:and] r)))]
                (if (= 1 (count ors))
                  [alphabet (first ors)]
                  [alphabet (into [:or] ors)]))))]
    (binding [*epsilon?* false]
      (topdown [start input]))))

(defn unger-epsilon-naive
  [{:keys [start rules] :as grammar} input]
  (letfn [(topdown [depth [alphabet input]]
            (Thread/sleep 100)
            (println (with-out-str
                       (doseq [_ (range depth)] (print " "))
                       (print alphabet input)))
            (if (or (terminal? alphabet) (epsilon? alphabet))
              [alphabet input]
              (when-some [ors (seq (for [[_ & pairs] (match-non-terminal rules [alphabet input])
                                         :let [r (map (partial topdown (inc depth)) pairs)]
                                         :when (every? some? r)]
                                     (into [:and] r)))]
                (if (= 1 (count ors))
                  [alphabet (first ors)]
                  [alphabet (into [:or] ors)]))))]
    (binding [*epsilon?* true]
      (topdown 0 [start input]))))

(comment
  ;; infinite loop
  (unger-epsilon-naive grammar-4-fig-4-3 "d"))

(defn unger-epsilon
  [{:keys [start rules] :as grammar} input]
  (letfn [(topdown [!searching [alphabet input :as p]]
            ;; cutoff on same question while topdown
            (when-not (!searching p)
              (if (or (terminal? alphabet) (epsilon? alphabet))
                [alphabet input]
                (when-some [ors (seq (for [[_ & pairs] (match-non-terminal rules [alphabet input])
                                           :let [r (map (partial topdown (conj !searching p)) pairs)]
                                           :when (every? some? r)]
                                       (into [:and] r)))]
                  (if (= 1 (count ors))
                    [alphabet (first ors)]
                    [alphabet (into [:or] ors)])))))]
    (binding [*epsilon?* true]
      (let [!searching #{}]
        (topdown !searching [start input])))))
