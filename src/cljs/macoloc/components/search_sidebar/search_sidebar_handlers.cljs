(ns macoloc.components.search-sidebar.search-sidebar-handlers
  (:require
    [macoloc.db  :refer [schema]]
    [re-frame.core :refer [register-handler path trim-v after debug]]
    [schema.core   :as s]))
