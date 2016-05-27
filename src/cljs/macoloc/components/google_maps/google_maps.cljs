(ns macoloc.components.google-maps.google-maps
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [re-frame.core :refer [dispatch
                                   subscribe]]
            [macoloc.components.open-map.open-map-subs]
            [macoloc.components.open-map.open-map-handlers]))
;; google.maps.LatLng.

(defn get-bounds [gmp]
  (if-not (nil? gmp)
    (let [ne (-> gmp .getBounds .getNorthEast)
          sw (-> gmp .getBounds .getSouthWest)]
      {:ne {:lat (.lat ne)
            :lng (.lng ne)}
       :sw {:lat (.lat sw)
            :lng (.lng sw)}})
    {}))

(defn add-marker! [{lat :lat long :lon} gmap]
  (if-let [pos (js/google.maps.LatLng. lat, long)]
    (let [map-options #js {"position" pos
                           "map" gmap}]
      (js/google.maps.Marker. map-options))))

(defn place-markers [rooms-data map]
  (doseq [r rooms-data]
    (add-marker! r map)))

(defn gmap-inner []
  (let [gmap (atom nil)
        on-idle #(js/google.maps.event.addListener %1 "idle" %2)]
    (r/create-class
     {:reagent-render (fn []
                        [:div.five.columns.map
                         [:div#map-canvas {:style {:height "100%" :position "static"}} "map"]])

      :component-did-mount (fn [this]
                             (let [data (:data (r/props this))
                                   node (.getElementById js/document "map-canvas")
                                   mp (js/google.maps.Map. node #js{"zoom" 13
                                                                    "center" #js{"lat" 48.856614 "lng" 2.3522219}})]
                               (println "gmap did mount")
                               (reset! gmap mp)
                               (place-markers data @gmap)
                               (on-idle mp #(dispatch [:update-bounds (get-bounds @gmap)]))))

      :component-did-update (fn [this]
                              (let [data (:data (r/props this))]
                                (println "gmap did update")
                                (place-markers data @gmap)))
      :display-name "Home google map"})))

(defn gmap-outer []
  (let [data (subscribe [:city-data])
        ;; opts (subscribe [:map-opts])
        ]
    (fn []
      [gmap-inner {:data @data}])))
