(ns ch4.grammars)

(defn epsilon?
  [alphabet]
  (= alphabet 'epsilon))

(defn terminal?
  [alphabet]
  (not (symbol? alphabet)))

(defn terminal-eq?
  "assume terminal be a `character` and input a `string`."
  [terminal input]
  (and (= (count input) 1) (= terminal (first input))))

(def grammar-4-fig-4-1
  {:start 'Expr
   :rules {'Expr   ['(Expr \+ Term) '(Term)]
           'Term   ['(Term \* Factor) '(Factor)]
           'Factor ['(\( Expr \)) '(\i)]}})

(def grammar-fig-4-6
  {:start 'Number
   :rules '{Number   [(Integer) (Real)]
            Integer  [(Digit) (Integer Digit)]
            Real     [(Integer Fraction Scale)]
            Fraction [(\. Integer)]
            Scale    [(\e Sign Integer) (Empty)]
            Digit    [(\0) (\1) (\2) (\3) (\4) (\5) (\6) (\7) (\8) (\9)]
            Sign     [(\+) (\-)]
            Empty    [(epsilon)]}})

(def grammar-fig-4-10
  {:start 'S
   :rules '{S [(L \a M)]
            L [(L M)
               (epsilon)]
            M [(M M)
               (epsilon)]}})

(def grammar-fig-4-15
  {:start 'Number
   :rules '{Number   [(\0) (\1) (\2) (\3) (\4) (\5) (\6) (\7) (\8) (\9)
                      (Integer Digit)
                      (N1 Scale')
                      (Integer Fraction)]
            N1       [(Integer Fraction)]
            Integer  [(\0) (\1) (\2) (\3) (\4) (\5) (\6) (\7) (\8) (\9)
                      (Integer Digit)]
            Fraction [(T1 Integer)]
            T1       [(\.)]
            Scale'   [(N2 Integer)]
            N2       [(T2 Sign)]
            T2       [(\e)]
            Digit    [(\0) (\1) (\2) (\3) (\4) (\5) (\6) (\7) (\8) (\9)]
            Sign     [(\+) (\-)]}})
