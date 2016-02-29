(ns three-cljs.entities.player-paddle
  (:require [three-cljs.entities.core :refer [Entity tick]]
            [three-cljs.world :refer [destination-pos]]
            [three-cljs.entities.aspects.mobile :refer [Mobile move can-move?]]
            [three-cljs.world :refer [is-inbounds?]]
            [three-cljs.webgl.meshes :refer [add-webgl-mesh]]))

(defrecord Player-Paddle [id ui-type material size pos])

(defn- new-player-paddle
  [pos]
  (->Player-Paddle :player-paddle :box {:color 0x1B32C0} [10 30 10] pos))

(defn create-player-paddle
  ([] (create-player-paddle [0 0 0]))
  ([pos]
   (let [player (assoc-in (new-player-paddle pos) [:speed] 5)]
     (add-webgl-mesh player)
     player)))

(extend-type Player-Paddle Entity
  (tick [this world]
        world))

(extend-type Player-Paddle Mobile
  (move [this world dest]
        {:pre [(can-move? this world dest)]}
        (assoc-in world [:entities :player-paddle :pos] dest))
  (can-move? [this world dest]
             (is-inbounds? world (:size this) dest)))

(defn move-player-paddle
  [world dir]
  (let [player (get-in world [:entities :player-paddle])
        dest (destination-pos (:pos player) dir (:speed player))]
    (cond
      (can-move? player world dest) (move player world dest)
      :else world)))
