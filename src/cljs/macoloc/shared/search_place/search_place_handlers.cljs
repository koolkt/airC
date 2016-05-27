(ns macoloc.shared.search-place.search-place-handlers
  (:require
    [macoloc.db  :refer [schema parse-place]]
    [re-frame.core :refer [register-handler path trim-v after debug dispatch]]
    [schema.core   :as s]
    [macoloc.resources :as service]))

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

(defn update-sugestions [db [_ predictions status]]
  (merge db {:search-predictions (js->clj predictions)
             :search-predictions-expand? true}))

(def as (js/google.maps.places.AutocompleteService.))

(defn get-sugestions [db [_ input key]]
  (if-not (or (empty? input) (= key 13))
    (do
      (-> as
          (.getQueryPredictions
           #js{:input input} (fn [predictions status]
                               (dispatch [:update-seugestions predictions status]))))
      db)
    (assoc db :search-predictions-expand? false)))

(defn search-selected [db [_ id]]
  (println "selected" id)
  (assoc db :search-predictions-expand? false))

(defn collapse-search [db]
  (assoc db :search-predictions-expand? false))

(register-handler
  :search-selected
  check-schema-mw
  search-selected)

(register-handler
  :search-input-event
  check-schema-mw
  get-sugestions)

(register-handler
  :update-seugestions
  check-schema-mw
  update-sugestions)

(register-handler
  :collapse-search
  check-schema-mw
  collapse-search)
