(ns three-cljs.entities.game-field
  (:require [three-cljs.webgl.meshes :refer [add-webgl-mesh]]))

(defrecord Field [id ui-type material size])

(defn- new-field
  [size]
  (->Field :game-field :plane {:color 0x4BD121} size))

(defn create-field
  [size]
  (let [field (new-field size)]
    (add-webgl-mesh field)
    field))
