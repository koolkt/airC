(ns macoloc.components.open-map.open-map
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [re-frame.core :refer [dispatch
                                   subscribe]]
            [macoloc.components.open-map.open-map-subs]
            [macoloc.components.open-map.open-map-handlers]))

;; "http://korona.geog.uni-heidelberg.de/tiles/roads/x={x}&y={y}&z={z}"
(defn add-layer [opm]
  (let [attr "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"]
    (-> js/L
        (.tileLayer "http://korona.geog.uni-heidelberg.de/tiles/roads/x={x}&y={y}&z={z}" #js {:attribution attr})
        (.addTo opm))))

(defn on-idle [opm callback]
  (-> opm
      (.on "moveend" callback)))
;; L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
;;     attribution: '&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors'
;; }).addTo(map);


(defn get-bounds [gmp]
  (if-not (nil? gmp)
    (let [ne (-> gmp .getBounds .getNorthEast)
          sw (-> gmp .getBounds .getSouthWest)]
      {:ne {:lat (aget ne "lat")
            :lng (aget ne "lng")}
       :sw {:lat (aget sw "lat")
            :lng (aget sw "lng")}})
    {}))

(defn add-marker! [{lat :lat long :lon} gmap]
  (let [options #js {"lat" lat "lon" long}]
    (-> (js/L.marker. options)
        (.addTo gmap))))

(defn place-markers [rooms-data map]
  (doseq [r rooms-data]
    (add-marker! r map)))

(defn open-map-inner []
  (let [omap (atom nil)]
    (r/create-class
     {:reagent-render (fn []
                        [:div.five.columns.map
                         [:div#map-canvas {:style {:height "100%"}} "map"]])

      :component-did-mount (fn [this]
                             (let [data (:data (r/props this))
                                   mp (-> (js/L.Map. "map-canvas")
                                          (.setView #js[48.856614, 2.3522219] 12))]
                               (add-layer mp)
                               (reset! omap mp)
                               (place-markers data @omap)
                               (on-idle mp #(dispatch [:update-bounds (get-bounds @omap)]))))

      :component-did-update (fn [this]
                              (let [data (:data (r/props this))]
                                (place-markers data @omap)))

      :display-name "Home google map"})))

(defn open-map-outer []
  (let [data (subscribe [:city-data])
        ;; opts (subscribe [:map-opts])
        ]
    (fn []
      [open-map-inner {:data @data}])))
