(ns ch2.grammars)

(def grammar-fig-2-27
  '{:start S
    :rules {S [(A B) (D E)]
            A [(\a)]
            B [(\b C)]
            C [(\c)]
            D [(\d F)]
            E [(\e)]
            F [(\f D)]}})
