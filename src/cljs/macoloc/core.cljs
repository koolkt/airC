(ns macoloc.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [macoloc.handlers]
            [macoloc.subs]
            [macoloc.routes :as routes]
            [macoloc.views :as views]
            [macoloc.shared.navbar.navbar :as nv]))

(defn mount-root []
  (reagent/render [nv/navbar]
                  (.getElementById js/document "navbar"))
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
