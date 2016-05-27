(ns user
  (:require [mount.core :as mount]
            [macoloc.figwheel :refer [start-fw stop-fw cljs]]
            macoloc.core))

(defn start []
  (mount/start-without #'macoloc.core/repl-server))

(defn stop []
  (mount/stop-except #'macoloc.core/repl-server))

(defn restart []
  (stop)
  (start))


