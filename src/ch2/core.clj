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
  (let [non-terminals (find-productive-non-terminals grammar)
        productive? (partial productive-rule? non-terminals)]
    (letfn [(keep-producive [[left rights]]
              (when (productive? left)
                (let [rights' (filterv #(every? productive? %) rights)]
                  (when (seq rights')
                    [left rights']))))]
      (->> (map keep-producive rules)
           (filter some?)
           (into {})
           (assoc grammar :rules)))))

(defn find-rechable-non-terminals
  [{:keys [rules start] :as grammar}]
  (letfn [(inference [rechable]
            (reduce
             (fn [rechable-acc t]
               (->> (rules t)
                    flatten
                    (filter (fn [v] (and (not (terminal? v))
                                         (not (epsilon? v)))))
                    (reduce conj rechable-acc)))
             rechable
             rechable))]
    (loop [rechable #{start}]
      (let [rechable' (inference rechable)]
        (if (= rechable rechable')
          rechable
          (recur rechable'))))))

(defn remove-non-rechable-rules
  [{:keys [rules] :as grammar}]
  (let [rechable (find-rechable-non-terminals grammar)]
    (->> rules
         (filter (comp rechable first))
         (into {})
         (assoc grammar :rules))))

(rcf/tests
 (find-productive-non-terminals grammar-fig-2-27)
 := (set '(S A B C E))

 (remove-non-productive-rules grammar-fig-2-27)
 := '{:start S
      :rules {S [(A B)]
              A [(\a)]
              B [(\b C)]
              C [(\c)]
              E [(\e)]}}

 (-> grammar-fig-2-27
     (remove-non-productive-rules)
     (remove-non-rechable-rules))
 := '{:start S
      :rules {S [(A B)]
              A [(\a)]
              B [(\b C)]
              C [(\c)]}})
