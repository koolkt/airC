(ns macoloc.handlers
  (:require [re-frame.core :as re-frame]
            [re-frame.core :refer [register-handler path trim-v after debug dispatch]]
            [macoloc.db  :refer [schema parse-place]]
            [macoloc.resources  :as service]
            [schema.core :as s]
            [macoloc.db :as db]))

(re-frame/register-handler
  :initialize-db
  (fn  [_ _]
    db/default-db))

(re-frame/register-handler
  :set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(defn check-and-throw
  "throw an exception if db doesn't match the schema."
  [a-schema db]
  (if-let [problems  (s/check a-schema db)]
    (throw (js/Error. (str "schema check failed: " problems)))))

;; Event handlers change state, that's their job. But what heppens if there's
;; a bug and they corrupt this state in some subtle way? This middleware is run after
;; each event handler has finished, and it checks app-db against a schema.  This
;; helps us detect event handler bugs early.
(def check-schema-mw (after (partial check-and-throw schema)))

(register-handler
  :merge-places
  check-schema-mw
  (fn [app-state  [_ bounds data]]
    (let [ds (:search app-state)
          n (merge ds {:bounds bounds :city-data {:paris data}})]
      (assoc app-state :search n))))

(register-handler
  :fetch-places
  (fn [db _]
    (let [bounds {:ne {:lat 48.9021449 :lng 2.4699208}
                  :sw {:lat 48.815573 :lng 2.225193}}]
      (service/data-api bounds
                        (fn [r]
                          (let [data (->> r (filter #(not (re-find #"fallbacks" (:th %)))))]
                            (dispatch [:merge-places bounds data]))))
      db)))
