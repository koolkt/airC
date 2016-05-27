(ns macoloc.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[macoloc started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[macoloc has shutdown successfully]=-"))
   :middleware identity})
