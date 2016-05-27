(ns macoloc.views
    (:require
     [re-frame.core :as re-frame]
     [reagent.core :as r]
     [macoloc.components.google-maps.google-maps :as gm]
     [macoloc.components.search-sidebar.search-sidebar :as sb]
     [macoloc.shared.search-place.search-place :as sp]))

(defn about []
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of colocbook... work in progress"]]])

(defn search-page-render []
  [:div.row
   [sb/search-sidebar]
   [gm/gmap-outer]])

(defn home-page-render []
  [:section#home-hero
   [sp/place-search-bar]
   [:div {:class "video"
          :data-src "/img/paris.jpg"
          :data-video "/video/paris.webm"
          :data-placeholder="/img/paris.jpg"}
    [:video#bgvid {:autoPlay true :loop true :poster "/img/paris.jpg"}
     [:source {:src "/video/paris.webm" :type "video/webm"}]
     [:source {:src "/video/paris.mp4" :type "video/mp4"}]]]])

(defn search []
  (r/create-class {:reagent-render search-page-render
                   :component-did-mount #(re-frame/dispatch [:fetch-places])}))

(defn home []
  (r/create-class {:reagent-render home-page-render
                   :component-did-mount #(aset js/window "onclick" (fn [e] (re-frame/dispatch [:collapse-search])))
                   :component-will-unmount #(re-frame/dispatch [:collapse-search])}))

;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home])
(defmethod panels :about-panel [] [about])
(defmethod panels :search [] [search])
(defmethod panels :default [] [:div])

(defn show-panel
  [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [show-panel @active-panel])))
