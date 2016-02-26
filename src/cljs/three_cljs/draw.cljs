(ns three-cljs.draw
  (:require [re-frame.core :refer [subscribe]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn draw
  [renderer scene camera]
  (let []
;;     (js/console.log renderer)
    (do
      ;;   draw THREE.JS scene
      (.render renderer scene camera)

      ;;   loop the draw() function
      (js/requestAnimationFrame draw)

      ;;   (ball-physics)

      ;;   reposition camera
;;       (set! (.-x camera.position) (- (.-x paddle1.position) 100))
;;       (set! (.-z camera.position) (+ (.-z paddle1.position) 100))
;;       (set! (.-z camera.rotation) (* -90 (/ (.-PI js/Math) 180)))
;;       (set! (.-y camera.rotation) (* -60 (/ (.-PI js/Math) 180)))
      ))
  )
