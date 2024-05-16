(ns ch2.core
  (:require
   [ch2.grammars :refer [grammar-fig-2-27]]
   [hyperfiddle.rcf :as rcf]))

(defn- epsilon?
  [alphabet]
  (= alphabet 'epsilon))

(defn- terminal?
  [alphabet]
  (not (symbol? alphabet)))

(defn- productive-rule?
  [known-productive alpabet]
  (or (terminal? alpabet)
      (epsilon? alpabet)
      (known-productive alpabet)))

(defn find-productive-non-terminals
  [{:keys [rules] :as grammar}]
  (letfn [(inference [known]
            (reduce-kv
             (fn [known k vs]
               (cond-> known
                 (some #(every? (partial productive-rule? known) %) vs)
                 (conj k)))
             known
             rules))]
    (loop [known-productive #{}]
      (let [known' (inference known-productive)]
        (if (= known-productive known')
          known-productive
          (recur known'))))))

(defn remove-non-productive-rules
  [{:keys [rules] :as grammar}]
  (let [productive-rules (find-productive-non-terminals grammar)
        productive? (partial productive-rule? productive-rules)]
    (letfn [(keep-producive [[left rights]]
              (when (productive? left)
                (let [rights' (filterv #(every? productive? %) rights)]
                  (when (seq rights')
                    [left rights']))))]
      (->> (map keep-producive rules)
           (filter some?)
           (into {})
           (assoc grammar :rules)))))

(rcf/tests
 (find-productive-non-terminals grammar-fig-2-27)
 := (set '(S A B C E))

 (remove-non-productive-rules grammar-fig-2-27)
 := '{:start S
      :rules {S [(A B)]
              A [(\a)]
              B [(\b C)]
              C [(\c)]
              E [(\e)]}})
