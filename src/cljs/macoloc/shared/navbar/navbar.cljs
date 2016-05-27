(ns macoloc.shared.navbar.navbar
  (:require [reagent.core :as r]
            [reagent.session :as session]))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar
       [:div.container
        [:button.navbar-toggler.hidden-sm-up
         {:on-click #(swap! collapsed? not)} "☰"]
        [:div.navbar-header
         [:a.navbar-brand {:href "#/"}
          [:img.header-logo {:src "/img/logo.svg"}]]]
        [:div.collapse.navbar-toggleable-xs
         (when-not @collapsed? {:class "in"})
         [:ul.nav.navbar-nav
          [nav-link "#/" "Home" :home collapsed?]
          [nav-link "#/search" "search" :search collapsed?]
          [nav-link "#/about" "About" :about collapsed?]]]]])))
