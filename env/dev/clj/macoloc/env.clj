(ns macoloc.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [macoloc.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[macoloc started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[macoloc has shutdown successfully]=-"))
   :middleware wrap-dev})
