(ns three-cljs.webgl.scene
  (:require [re-frame.core :refer [subscribe dispatch]]
            [three-cljs.webgl.objs :refer [webgl-objs]]
            [three-cljs.webgl.lights :refer [add-webgl-light]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn- update-webgl-pos [id [x y z]]
  (let [obj (.getObjectByName js/scene (str id))]
    (aset obj "position" "x" x)
    (aset obj "position" "y" y)
    (aset obj "position" "z" z)
    nil))

(defn- update-webgl-rot [id [x y z]]
  (let [obj (.getObjectByName js/scene (str id))]
    (aset obj "rotation" "x" x)
    (aset obj "rotation" "y" y)
    (aset obj "rotation" "z" z)
    nil))


(defn- watch-camera []
  (let [camera (subscribe [:game-camera])
        id (:id @camera)
        pos (reaction (:pos @camera))
        rot (reaction (:rot @camera))]
    (fn []
      (update-webgl-pos id @pos)
      (update-webgl-rot id @rot)
      )))

(defn- generate-scene []
  (let [config (subscribe [:config])
        [WIDTH HEIGHT] (:canvas-size @config)
        canvas-div (.getElementById js/document (:canvas-div-id @config))]
    ;;       Setup renderer
    (.setSize js/renderer WIDTH HEIGHT)
    (aset js/renderer "shadowMap" "enabled" true)
    (aset js/renderer "shadowMapSoft" true)
    (.appendChild canvas-div js/renderer.domElement)
    (dispatch [:webgl-scene-ready])
    (dispatch [:draw-scene])))

(defn webgl-scene []
  (let [scene-ready? (subscribe [:webgl-scene-initialized?])
        camera-ready? (subscribe [:webgl-camera-initialized?])]
    (fn []
      (if-not @scene-ready?
        (do
          (generate-scene)
          [:div#init-scene "Initializing WebGL Scene..."])
        [:div#scene
         (if-not @camera-ready?
           [:div#init-camera "Initializing WegGL Camera..."]
           [:div
            [watch-camera]
            [webgl-objs]])]))))
