(ns macoloc.components.open-map.open-map-subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(defn city-data-query [db [sid]]
  (assert (= sid :city-data))
  (let [city-name (-> @db
                      :search
                      :current-city)
        data (reaction (-> @db
                           :search
                           :city-data))]
    (reaction (if (nil? @data)
                []
                (city-name @data)))))

(register-sub
 :city-data
 city-data-query)

;; (register-sub
;;  :map-opts
;;  (fn [db [sid]]
;;    (assert (= sid :map-opts))
;;    (let [city-name (-> @db :paris)
;;          {:keys [lat :lat lng :lng]} (-> @db :cities :paris :center)]
;;      (println lat "cdq")
;;      {:center #js[lat lng]
;;       :zoom 13})))
