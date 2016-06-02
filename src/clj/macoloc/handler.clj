(ns macoloc.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [macoloc.layout :refer [error-page]]
            [macoloc.routes.home :refer [home-routes]]
            [macoloc.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [macoloc.env :refer [defaults]]
            [mount.core :as mount]
            [macoloc.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'service-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))

;; (def app (middleware/wrap-base #'app-routes))
