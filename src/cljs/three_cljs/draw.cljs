(ns three-cljs.draw
  (:require [re-frame.core :refer [subscribe dispatch]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn draw []
  (let [camera (.getObjectByName js/scene "my-camera")]

    ;;   loop the draw() function
    (js/requestAnimationFrame draw)

    ;;   draw THREE.JS scene
    (. js/renderer (render js/scene camera))

    ;;     update scene
    (dispatch [:tick-ball])
    (dispatch [:update-camera])
    ))
