(ns three-cljs.core
  (:require [reagent.core :as r]
            [cljsjs.three]
            [re-frame.core :refer [subscribe dispatch]]
            ;;             [three-cljs.debug]
            [three-cljs.draw :refer [draw]]
            [three-cljs.input :refer [add-event-listeners]]))


(defn- init-scene []
  (let [WIDTH (.-innerWidth js/window)
        HEIGHT (.-innerHeight js/window)
        viewport (.getElementById js/document "webGL-container")
        renderer (js/THREE.WebGLRenderer.)
        camera (js/THREE.PerspectiveCamera. 45 (/ WIDTH HEIGHT) .1 500)
        scene (js/THREE.Scene.)]
    (.setClearColor renderer 0xDDDDDD)
    (.setSize renderer WIDTH HEIGHT)
    (aset renderer "shadowMapEnabled" true)
    (aset renderer "shadowMapSoft" true)

    (.appendChild viewport (.-domElement renderer))

    (set! js/RENDERER renderer)
    (set! js/CAMERA camera)
    (set! js/SCENE scene)))

(defn- create-plane []
  (let [g (js/THREE.PlaneGeometry. 30 30 30)
        m (js/THREE.MeshLambertMaterial. #js {"color" 0xffffff})]
    (js/THREE.Mesh. g m)))

(defn- create-box []
  (let [g (js/THREE.BoxGeometry. 5 5 5)
        m (js/THREE.MeshLambertMaterial. #js {"color" 0xff3300})]
    (js/THREE.Mesh. g m)))

(defn- setup-scene
  []
  (let [axis (js/THREE.AxisHelper. 10)
        grid (js/THREE.GridHelper. 50 5)
        grid-color (js/THREE.Color. "rgb(255,0,0)")
        cube (create-box)
        plane (create-plane)
        spotlight (js/THREE.SpotLight. 0xffffff)]

    (.setColors grid grid-color 0x000000)
    (.add js/SCENE grid)
    (.add js/SCENE axis)

    (aset plane "rotation" "x" (* -0.5 (.-PI js/Math)))
    (aset plane "receiveShadow" true)
    (.add js/SCENE plane)

    (aset cube "position" "x" 2.5)
    (aset cube "position" "y" 2.5)
    (aset cube "position" "z" 2.5)
    (aset cube "castShadow" true)
    (.add js/SCENE cube)

    (aset spotlight "castShadow" true)
    (.set (.-position spotlight) 15 30 50)
    (.add js/SCENE spotlight)

    (aset js/CAMERA "position" "x" 40)
    (aset js/CAMERA "position" "y" 40)
    (aset js/CAMERA "position" "z" 40)

    (.lookAt js/CAMERA (.-position js/SCENE))


    (draw)
    ))

(defn ^:export init
  []
  (add-event-listeners)
  (init-scene)
  (setup-scene)
  )
