(ns ^:figwheel-no-load macoloc.app
  (:require [macoloc.core :as core]
            [devtools.core :as devtools]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :on-jsload core/mount-root)

(devtools/install! [:custom-formatters :sanity-hints])

(core/init)
