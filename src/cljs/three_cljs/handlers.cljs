(ns three-cljs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]))

(register-handler
  :initialize-db
  (fn [_ _]
    (let [scene (THREE.Scene.)
          camera (THREE.PerspectiveCamera. 50 (/ WIDTH HEIGHT) 0.1 10000)
          renderer (THREE.WebGLRenderer.)]
      {})))
