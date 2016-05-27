(ns macoloc.components.search-sidebar.search-sidebar-subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(defn inside-bounds? [{lat :lat lng :lon} {ne :ne sw :sw}]
  (and (and (< lat (:lat ne)) (> lat (:lat sw)))
       (and (< lng (:lng ne)) (> lng (:lng sw)))))

(defn places-in-bounds-query [db [sid]]
  (assert (= sid :places-in-view))
  (let [bounds (-> @db :search :bounds)
        data (-> @db
                 :search
                 :city-data
                 :paris)]
    (reaction  (->> data
                    (filter #(inside-bounds? % bounds))))))

(register-sub
 :places-in-view
 places-in-bounds-query)
