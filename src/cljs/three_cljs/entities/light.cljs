(ns three-cljs.entities.light
  (:require [three-cljs.webgl.lights :refer [add-webgl-light]]))

(defrecord Light [id type hex props pos])

(defn- new-light
  [id type hex props pos]
  (->Light id type hex props pos))

(defn create-light
  ([id type hex props] (create-light id type hex props [0 0 0]))
  ([id type hex props pos]
   (let [light (new-light id type hex props pos)]
     (add-webgl-light light)
     light)))
