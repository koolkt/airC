(ns macoloc.shared.search-place.search-place
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [macoloc.shared.search-place.search-place-handlers]
            [macoloc.shared.search-place.search-place-subs]))

(defn place-search-bar []
  (let [sug (subscribe [:places-pred])
        expand-pred? (subscribe [:search-p-expand?])
        input-event #(dispatch [:search-input-event (-> % .-target .-value) (-> % .-keyCode)])]
    [:div.search-box-hero
     [:input#search-location-hero {:type "text"
                                   :placeholder "Search"
                                   :on-click input-event
                                   :on-key-press
                                   (fn [e]
                                     (let [key (-> e .-charCode)
                                           input (-> e .-target .-value)
                                           enter 13]
                                       (when (and (= key enter)
                                                  (not-empty input))
                                         (dispatch [:search-selected (get (first @sug) "place_id")]))))
                                   :on-key-up input-event}]
     (if (or (empty? @sug)
             (not @expand-pred?))
       [:ul]
       [:ul.search-suggestions {:style {:background "white"}}
        (map (fn [o]
               (let [place (get o "description")
                     id (get o "place_id")]
                 [:a {:href "#" :key place} [:li {:on-click #(dispatch [:search-selected id])} place]]))
             @sug)])]))
