(ns macoloc.components.open-map.open-map-handlers
  (:require
   [macoloc.db  :refer [schema]]
   [re-frame.core :refer [register-handler path trim-v after debug]]
   [schema.core   :as s]))

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

(defn need-more-data? [{one :ne osw :sw} {ne :ne sw :sw}]
  (if (nil? osw)
    true
    (or (or (< (:lat one) (:lat ne)) (> (:lat osw) (:lat sw)))
        (or (< (:lng one) (:lng ne)) (> (:lng osw) (:lng sw))))))

;; (when (need-more-data? @old-b @bounds)
;;   (reset! old-b @bounds)
;;   (service/data-api @bounds
;;                     (fn [r]
;;                       (let [new-data (get r "markers")
;;                             ;; old-data @data
;;                             ]
;;                         (reset! data new-data)
;;                         (opm/place-markers new-data @gmap)))))

(register-handler
  :update-bounds
  check-schema-mw
  (fn [app-state  [_ bounds]]
    (let [search (-> app-state :search)
          new-search (merge search {:bounds bounds})]
      (assoc app-state :search new-search))))
