(ns three-cljs.draw
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn draw []
  (let [camera (subscribe [:game-camera])
        camera-obj (.getObjectByName js/scene (str (:id @camera)))
        running? (subscribe [:game-running?])]

    ;;   loop the draw() function
    (js/requestAnimationFrame draw)

    ;;   draw THREE.JS scene
    (. js/renderer (render js/scene camera-obj))

    (if running?
      ;;     tick next frame
        (dispatch [:tick]))
    ))
