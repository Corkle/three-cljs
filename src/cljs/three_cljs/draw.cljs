(ns three-cljs.draw
  (:require [re-frame.core :refer [subscribe dispatch]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn draw []
  (let [camera (.getObjectByName js/scene "my-camera")]
    ;;     (js/console.log js/scene)
    ;;   loop the draw() function
    (js/requestAnimationFrame draw)

    ;;   draw THREE.JS scene
    (. js/renderer (render js/scene camera))
    ;;       (js/console.log "Draw")


    ;;     update scene
    (dispatch [:tick-ball])


    ;;   reposition camera
    ;;       (set! (.-x camera.position) (- (.-x paddle1.position) 100))
    ;;       (set! (.-z camera.position) (+ (.-z paddle1.position) 100))
    ;;       (set! (.-z camera.rotation) (* -90 (/ (.-PI js/Math) 180)))
    ;;       (set! (.-y camera.rotation) (* -60 (/ (.-PI js/Math) 180)))
    ))
