(ns three-cljs.entities.opponent-paddle
  (:require [three-cljs.entities.core :refer [Entity tick]]
            [three-cljs.webgl.meshes :refer [add-webgl-mesh]]))

(defrecord Opponent-Paddle [id ui-type material size pos])

(defn- new-opponent-paddle
  [pos]
  (->Opponent-Paddle :opponent-paddle :box {:color 0xFF4045} [10 30 10] pos))

(defn create-opponent-paddle
  ([] (create-opponent-paddle [0 0 0]))
  ([pos]
   (let [opponent (new-opponent-paddle pos)]
     (add-webgl-mesh opponent)
     opponent)))

(extend-type Opponent-Paddle Entity
  (tick [this world]
        world))
