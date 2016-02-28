(ns three-cljs.draw
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn draw []
  (let [camera (.getObjectByName js/scene "my-camera")
        running? (subscribe [:game-running?])]

    ;;   loop the draw() function
    (js/requestAnimationFrame draw)

    ;;   draw THREE.JS scene
    (. js/renderer (render js/scene camera))

    (if running?
      (do
        ;;     update scene
        (dispatch [:tick-ball])
        (dispatch [:update-camera])))
    ))
