(ns macoloc.db
  (:require [cljs.reader]
            [schema.core  :as s :include-macros true]
            [schema.coerce :as coerce]))

(def lat js/Number)

(def lng js/Number)

(def place {:id s/Int     :ut s/Str
            :lat lat      :lon lng
            :as s/Any     :hm s/Int
            :cr s/Int     :las s/Str
            :la s/Str     :ls s/Int
            :lt s/Str     :ca s/Str
            :lo s/Int     :ac s/Str
            :th s/Str     :ff s/Any
            :pl s/Any     :pn s/Any})


(def parse-place
  (coerce/coercer [place] coerce/json-coercion-matcher))

(def lat-lng {(s/required-key :lat) lat (s/required-key :lng) lng})

(def bounds {(s/required-key :ne) lat-lng
             (s/required-key :sw) lat-lng})

(def schema {:search {:city-data {s/Keyword [place]}
                      :cities {s/Keyword {:center lat-lng :bounds bounds}}
                      :bounds bounds
                      :current-city s/Keyword}
             :search-input s/Str
             :search-predictions s/Any
             :search-predictions-expand? s/Bool
             :active-panel s/Keyword})

(def default-db {:search {:city-data {}
                          :cities {:paris {:center {:lat 48.856614 :lng 2.3522219}
                                           :bounds {:ne {:lat 48.9021449 :lng 2.4699208}
                                                    :sw {:lat 48.815573 :lng 2.225193}}}}
                          :bounds {:ne {:lat 0 :lng 0} :sw {:lat 0 :lng 0}}
                          :current-city :paris}
                 :search-input ""
                 :search-predictions []
                 :search-predictions-expand? false})
