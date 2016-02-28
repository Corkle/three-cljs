(ns three-cljs.webgl.objs
  (:require [re-frame.core :refer [subscribe]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn- update-webgl-obj [obj-name prop-vals]
  (if-let [obj (.getObjectByName js/scene obj-name)]
    (doseq [prop-val prop-vals]
      (apply (partial aset obj) prop-val))
    (js/console.log "No object: " obj-name)))

(defn- update-webgl-pos [name [x y z]]
  (update-webgl-obj name [["position" "x" x]
                          ["position" "y" y]
                          ["position" "z" z]]))

(defn- ball-obj []
  (let [ball (subscribe [:ball])
        pos (reaction (:pos @ball))]
    (fn []
      (update-webgl-pos "my-ball" @pos))))

(defn- player-paddle-obj []
  (let [player-paddle (subscribe [:player-paddle])
        pos (reaction (:pos @player-paddle))]
    (fn []
      (update-webgl-pos "player-paddle" @pos))))

(defn- opponent-paddle-obj []
  (let [opponent-paddle (subscribe [:opponent-paddle])
        pos (reaction (:pos @opponent-paddle))]
    (fn []
      (update-webgl-pos "opponent-paddle" @pos))))

(defn- camera-obj []
  (let [camera (subscribe [:camera])
        pos (reaction (:pos @camera))
        rotation (reaction (:rotation @camera))]
    (fn []
      (let [[pos-x pos-y pos-z] @pos
            [rotation-x rotation-y rotation-z] @rotation]
        (update-webgl-obj "my-camera" [["position" "x" pos-x]
                                       ["position" "y" pos-y]
                                       ["position" "z" pos-z]
                                       ["rotation" "x" rotation-x]
                                       ["rotation" "y" rotation-y]
                                       ["rotation" "z" rotation-z]])))))

(defn webgl-objs []
  [:div#objs
   [camera-obj]
   [player-paddle-obj]
   [opponent-paddle-obj]
   [ball-obj]])
