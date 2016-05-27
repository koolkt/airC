(ns macoloc.shared.search-place.search-place-subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(defn places-pred-query [db [sid]]
  (assert (= sid :places-pred))
  (let [sug (-> @db :search-predictions)]
    (reaction sug)))

(defn places-p-expand-query [db [sid]]
  (assert (= sid  :search-p-expand?))
  (reaction (-> @db :search-predictions-expand?)))

(register-sub
 :places-pred
 places-pred-query)


(register-sub
 :search-p-expand?
 places-p-expand-query)
