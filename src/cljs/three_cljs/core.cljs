(ns three-cljs.core
  (:require [reagent.core :as r]
            ;;             [cljsjs.three]
            [re-frame.core :refer [subscribe dispatch]]
            ;;             [three-cljs.debug]
            [three-cljs.draw :refer [draw]]
            [three-cljs.input :refer [add-event-listeners]]))

(defn- init-scene []
  (let [WIDTH (.-innerWidth js/window)
        HEIGHT (.-innerHeight js/window)
        viewport (.getElementById js/document "webGL-container")
        renderer (js/THREE.WebGLRenderer. #js {"antialias" true})
        camera (js/THREE.PerspectiveCamera. 45 (/ WIDTH HEIGHT) .1 500)
        scene (js/THREE.Scene.)]
    (.setClearColor renderer 0xDDDDDD)
    (.setSize renderer WIDTH HEIGHT)
    (aset renderer "shadowMap" "enabled" true)
    (aset renderer "shadowMapSoft" true)

    (.appendChild viewport (.-domElement renderer))

    (set! js/RENDERER renderer)
    (set! js/CAMERA camera)
    (set! js/SCENE scene)
    (set! js/controls (THREE.OrbitControls. js/CAMERA (.-domElement js/RENDERER)))))



(defn- create-plane []
  (let [g (js/THREE.PlaneGeometry. 100 100 100)
        m (js/THREE.MeshLambertMaterial. #js {"color" 0xffffff})]
    (js/THREE.Mesh. g m)))

(defn- create-box []
  (let [g (js/THREE.BoxGeometry. 5 5 5)
        m (js/THREE.MeshLambertMaterial. #js {"color" 0xff3300})]
    (js/THREE.Mesh. g m)))

(defn- create-torus []
  (let [g (js/THREE.TorusKnotGeometry. 3 1 64 64)
        m (js/THREE.MeshPhongMaterial. #js {"color" 0xffff00})]
    (js/THREE.Mesh. g m)))

(defn- setup-scene
  []
  (let [axis (js/THREE.AxisHelper. 10)
        grid (js/THREE.GridHelper. 50 5)
        grid-color (js/THREE.Color. "rgb(255,0,0)")
        cube (create-box)
        plane (create-plane)
        torus-knot (create-torus)
        spotlight (js/THREE.SpotLight. 0xffffff)]

    ;;     Add Helpers
    (.setColors grid grid-color 0x000000)
    (.add js/SCENE grid)
    (.add js/SCENE axis)

    ;;     Add Plane
    (aset plane "rotation" "x" (* -0.5 (.-PI js/Math)))
    (aset plane "receiveShadow" true)
    (.add js/SCENE plane)

    ;;     Add Cube
    (aset cube "name" "cube")
    (aset cube "position" "x" 2.5)
    (aset cube "position" "y" 4)
    (aset cube "position" "z" 2.5)
    (aset cube "castShadow" true)
    (.add js/SCENE cube)

    ;;     Add TorusKnot
    (aset torus-knot "name" "torus-knot")
    (aset torus-knot "position" "x" -15)
    (aset torus-knot "position" "y" 6)
    (aset torus-knot "position" "z" 2.5)
    (aset torus-knot "castShadow" true)
    (.add js/SCENE torus-knot)

    (aset js/CAMERA "position" "x" 40)
    (aset js/CAMERA "position" "y" 40)
    (aset js/CAMERA "position" "z" 40)
    (.lookAt js/CAMERA (.-position js/SCENE))

    (set! js/guiControls #js {"rotationX" 0.01,
                              "rotationY" 0.01,
                              "rotationZ" 0.01,
                              "lightX" 20,
                              "lightY" 35,
                              "lightZ" 40,
                              "intensity" 1,
                              "distance" 0,
                              "angle" 1.570,
                              "exponent" 0,
                              "shadowCameraNear" 10,
                              "shadowCameraFar" 100,
                              "shadowCameraFov" 50,
                              "shadowCameraVisible" true,
                              "shadowMapWidth" 1028,
                              "shadowMapHeight" 1028,
                              "shadowBias" 0,
                              "shadowDarkness" 0.5,
                              "target" ""})

    (aset spotlight "name" "spotlight")
    (aset spotlight "castShadow" true)
    (.set (.-position spotlight) 15 30 50)
    (aset spotlight "intensity" (.-intensity js/guiControls))
    (aset spotlight "distance" (.-distance js/guiControls))
    (aset spotlight "angle" (.-angle js/guiControls))
    (aset spotlight "exponent" (.-exponent js/guiControls))
    (aset spotlight "shadow" "camera" "near" (.-shadowCameraNear js/guiControls))
    (aset spotlight "shadow" "camera" "far" (.-shadowCameraFar js/guiControls))
    (aset spotlight "shadow" "camera" "fov" (.-shadowCameraFov js/guiControls))
    (aset spotlight "shadow" "bias" (.-shadowBias js/guiControls))
    (.add js/SCENE spotlight)

    (set! js/shadowHelper (js/THREE.CameraHelper. (aget spotlight "shadow" "camera")))
    (.add js/SCENE js/shadowHelper)


    (set! js/datGUI (DAT.GUI.))
    (.add js/datGUI js/guiControls "rotationX" 0 .3)
    (.add js/datGUI js/guiControls "rotationY" 0 .3)
    (.add js/datGUI js/guiControls "rotationZ" 0 .3)
    (.add js/datGUI js/guiControls "lightX" -60 180)
    (.add js/datGUI js/guiControls "lightY" 0 180)
    (.add js/datGUI js/guiControls "lightZ" -60 180)
    (-> (.add js/datGUI js/guiControls "target")
        (.onChange (fn [value]
                     (if (= value "cube")
                       (aset spotlight "target" cube)
                       (if (= value "torus")
                         (aset spotlight "target" torus-knot))))))
    (-> (.add js/datGUI js/guiControls "intensity" 0.01 5)
        (.onChange #(aset spotlight "intensity" %)))
    (-> (.add js/datGUI js/guiControls "distance" 0 1000)
        (.onChange #(aset spotlight "distance" %)))
    (-> (.add js/datGUI js/guiControls "angle" 0.001 1.570)
        (.onChange #(aset spotlight "angle" %)))
    (-> (.add js/datGUI js/guiControls "exponent" 0 50)
        (.onChange #(aset spotlight "exponent" %)))
    (-> (.add js/datGUI js/guiControls "shadowCameraNear" 0 100)
        (.onChange #(do
                      (aset spotlight "shadow" "camera" "near" %)
                      (.updateProjectionMatrix (aget spotlight "shadow" "camera"))
                      (.update js/shadowHelper))))
    (-> (.add js/datGUI js/guiControls "shadowCameraFar" 0 5000)
        (.onChange #(do
                      (aset spotlight "shadow" "camera" "far" %)
                      (.updateProjectionMatrix (aget spotlight "shadow" "camera"))
                      (.update js/shadowHelper))))
    (-> (.add js/datGUI js/guiControls "shadowCameraFov" 1 180)
        (.onChange #(do
                      (aset spotlight "shadow" "camera" "fov" %)
                      (.updateProjectionMatrix (aget spotlight "shadow" "camera"))
                      (.update js/shadowHelper))))
    (-> (.add js/datGUI js/guiControls "shadowBias" 0 1)
        (.onChange #(do
                      (aset spotlight "shadow" "bias" %)
                      (.updateProjectionMatrix (aget spotlight "shadow" "camera"))
                      (.update js/shadowHelper))))
    (.close js/datGUI)

    (set! js/stats (js/Stats.))
    (aset js/stats "domElement" "style" "position" "absolute")
    (aset js/stats "domElement" "style" "left" 0)
    (aset js/stats "domElement" "style" "top" 0)
    (.appendChild (.getElementById js/document "stats") (.-domElement js/stats))

    (draw)
    ))



(defn ^:export init
  []
  (init-scene)
  (setup-scene)
  (add-event-listeners)
  )
