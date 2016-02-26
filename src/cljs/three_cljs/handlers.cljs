(ns three-cljs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [three-cljs.draw :refer [draw]]))

(register-handler
  :initialize-db
  (fn [_ [_ new-db]]
    new-db))

(register-handler
  :draw-scene
  (fn [db _]
    (let [threejs-objects (:threejs-objects db)
          renderer (:renderer threejs-objects)
          scene (:scene threejs-objects)
          camera (:camera threejs-objects)]
      (draw renderer scene camera)
      db)))

(register-handler
  :add-to-scene
  (fn [db [_ obj]]
    (let [scene (get-in db [:threejs-objects :scene])]
    (assoc-in db [:threejs-objects :scene] (.add scene obj)))
;;     (let [scene (get-in db [:threejs-objects :scene])]
;;       (.add scene obj))
    ))
