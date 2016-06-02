(ns macoloc.scrape
  (:require    [schema.core :as s]
               [clj-http.client :as client]))

(def lacarte-des-colocs "http://www.lacartedescolocs.fr/listings/update_map_results?")
(def user-agent "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36")


;; (def place {:id s/Int     :ut s/Str
;;             :lat lat      :lon lng
;;             :as s/Any     :hm s/Int
;;             :cr s/Int     :las s/Str
;;             :la s/Str     :ls s/Int
;;             :lt s/Str     :ca s/Str
;;             :lo s/Int     :ac s/Str
;;             :th s/Str     :ff s/Any
;;             :pl s/Any     :pn s/Any})

(defn cartcoloc-api [ne sw]
  (let [{nelat :lat nelng :lng} ne
        {swlat :lat swlng :lng} sw]
    {"viewport[swLat]" swlat
     "viewport[swLon]" swlng
     "viewport[neLat]" nelat
     "viewport[neLon]" nelng
     "viewport[zoom]" "16"
     "filters[mode]" "map"
     "filters[queryOrder]" "created_at DESC"
     "filters[queryOffset]" "30"
     "whitelisted" ""
     "locale" "fr"
     "device" ""}))

(defn coloc-data [{:keys [ne sw]}]
  (-> (client/get lacarte-des-colocs
                  {:query-params (cartcoloc-api ne sw)
                   :headers {:Host "www.lacartedescolocs.fr"
                             :User-Agent user-agent
                             :Accept "*/*"}
                   :as :json})
      :body
      :markers))
