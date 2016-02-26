(ns three-cljs.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [three-cljs.handlers]
            [three-cljs.input :refer [add-event-listeners]]
            [three-cljs.draw :refer [draw]]
            [three-cljs.meshes :refer [create_plane create_sphere create_paddle]]))

(def WIDTH 640)
(def HEIGHT 360)

(def scene (THREE.Scene.))
(def camera (THREE.PerspectiveCamera. 50 (/ WIDTH HEIGHT) 0.1 10000))
(def renderer (THREE.WebGLRenderer.))

(def point-light (THREE.PointLight. 0xF8D898))

(def field-width 400)
(def field-height 200)
(def paddle-width 10)
(def paddle-height 30)
(def paddle-depth 10)
(def ballDirX 1)
(def ballDirY 1)
(def ballSpeed 2)

(def ball (create_sphere 5 10 16))
(def plane (create_plane field-width field-height 10))
(def paddle1 (create_paddle paddle-width paddle-height paddle-depth 1 0x1B32C0))
(def paddle2 (create_paddle paddle-width paddle-height paddle-depth 1 0xFF4045))

(defn ball-physics []
  (let [ball-y (.-y ball.position)
        ball-x (.-x ball.position)]
    (do
      (if (or (<= ball-y (- (/ field-height 2)))
              (>= ball-y (/ field-height 2)))
        (set! ballDirY (- ballDirY)))
      (set! (.-x ball.position) (+ ball-x (* ballDirX ballSpeed)))
      (set! (.-y ball.position) (+ ball-y (* ballDirY ballSpeed))))))

(defn ^:export init
  []
  (let [c (.getElementById js/document "gameCanvas")]

    (rf/dispatch [:initialize-db])

;;     start renderer
    (.setSize renderer WIDTH HEIGHT)
;;     attach render-suplied DOM element
    (.appendChild c renderer.domElement)
;;     add the camera to the scene
    (.add scene camera)
;;     set default position for the camera
    (set! (.-z camera.position) 320)

;;     set the light's position
    (set! (.-x point-light.position) 500)
    (set! (.-y point-light.position) 600)
    (set! (.-z point-light.position) 900)
    (set! (.-intensity point-light) 2.5)
    (set! (.-distance point-light) 10000)
;;     add the light to the scene
    (.add scene point-light)

;;     add a sphere to the scene
    (.add scene ball)
;;     add the playing surface plane
    (.add scene plane)
;;     add paddles
    (.add scene paddle1)
    (.add scene paddle2)

;;     set paddles on each side of the table
    (set! (.-x paddle1.position) (+ (/ field-width -2) paddle-width))
    (set! (.-x paddle2.position) (+ (/ field-width 2) (- paddle-width)))

;;     lift paddles over playing surface
    (set! (.-z paddle1.position) paddle-depth)
    (set! (.-z paddle2.position) paddle-depth)

    (add-event-listeners)
    (draw)))
