(ns three-cljs.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(declare scene camera renderer)

(defn draw
  []
  (js/requestAnimationFrame draw))

(defn ^:export init
  []
  (def WIDTH 640)
  (def HEIGHT 360)
  (def VIEW_ANGLE 50)
  (def ASPECT (/ WIDTH HEIGHT))
  (def NEAR 0.1)
  (def FAR 10000)

  (def scene (r/atom (THREE.Scene.)))
  (def camera (THREE.PerspectiveCamera. VIEW_ANGLE ASPECT NEAR FAR))
  (def renderer (THREE.WebGLRenderer.))

  (draw))
