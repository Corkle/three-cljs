(ns three-cljs.entities.ball
  (:require [three-cljs.entities.core :refer [Entity tick]]
            [three-cljs.webgl.meshes :refer [add-webgl-mesh]]))

(defrecord Ball [id ui-type material size pos])

(defn- new-ball
  [pos]
  (->Ball :game-ball :sphere {:color 0xD43001} [5 10 16] pos))

(defn create-ball
  ([] (create-ball [0 0 10]))
  ([pos]
   (let [ball (new-ball pos)]
     (add-webgl-mesh ball)
     ball)))

(defn- get-dir-y [y dir-y field-height]
  (if (or (<= y (- (/ field-height 2)))
          (>= y (/ field-height 2)))
    (- dir-y)
    dir-y))

(extend-type Ball Entity
  (tick [this world]
        (let [[x y z] (:pos this)
              speed (:speed this)]
          (if (not (= 0 speed))
            (let [[field-width field-height] (get-in world [:field :size])
                  [dir-x dir-y] (:dir this)
                  dir-y (get-dir-y y dir-y field-height)
                  new-x (+ x speed)
                  new-y (+ y (* dir-y speed))]
              (assoc-in world [:entities :ball]
                        (-> this
                            (assoc-in [:pos] [new-x new-y z])
                            (assoc-in [:dir] [dir-x dir-y]))))
            world))))
