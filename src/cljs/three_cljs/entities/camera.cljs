(ns three-cljs.entities.camera
  (:require [three-cljs.webgl.cameras :refer [add-webgl-camera]]))

(defrecord Camera [id type props pos rot])

(defn- new-camera
  [id type props pos rot]
  (->Camera id type props pos rot))

(defn create-camera
  ([id type props] (create-camera id type props [0 0 0] [0 0 0]))
  ([id type props pos] (create-camera id type props pos [0 0 0]))
  ([id type props pos rot]
   (let [camera (new-camera id type props pos rot)]
     (add-webgl-camera camera)
     camera)))
