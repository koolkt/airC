(ns macoloc.resources
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [macoloc.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console
    (str "something bad happened: " status " " status-text)))

(defn data-api [params hand]
  (POST "/api/cc"
        {:headers {"Accept" "application/transit+json"}
         :params params
         :handler hand
         :error-handler error-handler}))
