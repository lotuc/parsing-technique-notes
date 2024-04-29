(ns ch4.ch4-02-cyk)

(def grammar-fig-4-6
  {:start 'Number
   :rules '{Number   [(Integer) (Real)]
            Integer  [(Digit) (Integer Digit)]
            Real     [(Integer Fraction Scale)]
            Fraction [(\. Integer)]
            Scale    [(\e Sign Integer) (Empty)]
            Digit    [((\0) (\1) (\2) (\3) (\4) (\5) (\6) (\7) (\8) (\9))]
            Sign     [(\+) (\-)]
            Empty    [(epsilon)]}})

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
