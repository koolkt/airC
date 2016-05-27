(ns macoloc.components.search-sidebar.search-sidebar
  (:require [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]
            [macoloc.components.search-sidebar.search-sidebar-subs]))

(defn card-image [url price]
  [:div.panel-image
   [:a.panel-image-link {:href "#"}
    [:div.image-container
     [:img.card-image-top.img-responsive-height {:src url}]]]
   [:div.controls
    [:div.target-prev [:i.icon.left-control]]
    [:div.target-next [:i.icon.right-control]]]
   [:div.panel-overlay-center-left
    [:div.price-label
     [:span.currency-prefix "€"]
     [:span.price-amount price]]]
   [:div.panel-overlay-top-right [:i.icon-heart]]])

(defn medium-image [url]
  (clojure.string/replace url "small.jpg" "medium.jpg"))

(defn create-place-card [{street :as hood :ac
                          th-url :th  price :cr id :id}]
  [:div.six.columns.cards {:key id}
   [:div.card.listing
    [card-image (medium-image th-url) price]
    [:div.card-block
     [:h4.card-title (str "Price: " price)]
     [:p.card-text (str street " " hood)]]]])

(defn create-place-row [plc]
  (let [{k :ut} (first plc)]
    [:div.row {:key k}
     (if (empty? (second plc))
       (create-place-card (first plc))
       (map create-place-card plc))]))

(defn ss-render [places]
  [:div.seven.columns.search-sidebar
   (if (not-empty @places)
     (take 20 (->> @places
                   (partition 2 2 [{}])
                   (map create-place-row)))
     [:h1 "No Places Found"])])

(defn search-sidebar []
  (let [places (subscribe [:places-in-view])]
   (ss-render places)))
