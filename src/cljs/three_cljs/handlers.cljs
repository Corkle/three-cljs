(ns three-cljs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [three-cljs.draw :refer [draw]]
            [three-cljs.meshes :refer [create_plane create_sphere create_paddle]]))

(register-handler
  :initialize-db
  (fn [_ _]
    (let [camera-rotation [0 (* -60 (/ (.-PI js/Math) 180)) (* -90 (/ (.-PI js/Math) 180))]]
      {:config {:canvas-size [640 360]}
       :game {:field-size [400 200]
              :camera {:pos [0 0 0]
                       :rotation camera-rotation}
              :entities {:ball {:pos [0 0 0]
                                :dir [1 1]
                                :speed 2}}}})))

;; --------- WegGL Initializers


(defn- initialize-camera [[VIEW_ANGLE APSECT NEAR FAR] [x y z] name]
  (let [camera (THREE.PerspectiveCamera. VIEW_ANGLE APSECT NEAR FAR)]
    (set! (.-x camera.position) x)
    (set! (.-y camera.position) y)
    (set! (.-z camera.position) z)
    (set! (.-name camera) "my-camera")
    camera))

(defn- initialize-light [hex intensity distance [x y z] name]
  (let [point-light (THREE.PointLight. hex intensity distance)]
    (set! (.-x point-light.position) x)
    (set! (.-y point-light.position) y)
    (set! (.-z point-light.position) z)
    point-light))

(register-handler
  :initialize-scene
  (fn [db _]
    (let [[WIDTH HEIGHT] (get-in db [:config :canvas-size])
          c (.getElementById js/document "gameCanvas")
          camera (initialize-camera [50 (/ WIDTH HEIGHT) 0.1 10000] [0 0 320] "my-camera")
          point-light (initialize-light 0xF8D898 2.5 10000 [500 600 900] "my-light")]

      ;;       Setup renderer
      (.setSize js/renderer WIDTH HEIGHT)
      (.appendChild c js/renderer.domElement)

      (dispatch [:add-to-scene [camera point-light]])
      (dispatch [:draw-scene])
      (dispatch [:load-game])
      db)))

(register-handler
  :load-game
  (fn [db _]
    (let [[field-width field-height] (get-in db [:game :field-size])
          paddle-width 10
          paddle-height 30
          paddle-depth 10
          ball (create_sphere 5 10 16 "my-ball" {:color 0xD43001})
          plane (create_plane (* .95 field-width) field-height 10 [0 0 0] "my-plane" {:color 0x4BD121})
          paddle1 (create_paddle paddle-width paddle-height paddle-depth 1 [0 0 0] "player-paddle" {:color 0x1B32C0})
          paddle2 (create_paddle paddle-width paddle-height paddle-depth 1 [0 0 0] "opponent-paddle" {:color 0xFF4045})]

      (dispatch [:add-to-scene [ball plane paddle1 paddle2]])
      (dispatch [:update-player-paddle-webgl])
      (dispatch [:update-opponent-paddle-webgl])
      (-> db
          (assoc-in [:game :entities :player-paddle] {:pos [(+ (/ field-width -2) 10) 0 0]
                                                      :size [10 30 10]})
          (assoc-in [:game :entities :opponent-paddle] {:pos [(+ (/ field-width 2) -10) 0 0]
                                                        :size [10 30 10]})))))

;; --------- WegGL Events

(register-handler
  :add-to-scene
  (fn [db [_ objs]]
    (doseq [obj objs]
      (.add js/scene obj))
    db))

(register-handler
  :draw-scene
  (fn [db _]
    (draw)
    db))

(defn- update-webgl-obj [obj-name prop-vals]
  (let [obj (.getObjectByName js/scene obj-name)]
    (doseq [prop-val prop-vals]
      (apply (partial aset obj) prop-val))))

(register-handler
  :update-player-paddle-webgl
  (fn [db _]
    (let [player-paddle (get-in db [:game :entities :player-paddle])
          [pos-x pos-y pos-z] (:pos player-paddle)]
      (update-webgl-obj "player-paddle" [["position" "x" pos-x]
                                         ["position" "y" pos-y]
                                         ["position" "z" pos-z]])
      db)))

(register-handler
  :update-opponent-paddle-webgl
  (fn [db _]
    (let [opponent-paddle (get-in db [:game :entities :opponent-paddle])
          [pos-x pos-y pos-z] (:pos opponent-paddle)]
      (update-webgl-obj "opponent-paddle" [["position" "x" pos-x]
                                           ["position" "y" pos-y]
                                           ["position" "z" pos-z]])
      db)))

(register-handler
  :update-camera-webgl
  (fn [db _]
    (let [camera (get-in db [:game :camera])
          [pos-x pos-y pos-z] (:pos camera)
          [rotation-x rotation-y rotation-z] (:rotation camera)]
      (update-webgl-obj "my-camera" [["position" "x" pos-x]
                                     ["position" "y" pos-y]
                                     ["position" "z" pos-z]
                                     ["rotation" "x" rotation-x]
                                     ["rotation" "y" rotation-y]
                                     ["rotation" "z" rotation-z]])
      db)))

;; --------- Entity Events

(defn- get-ball-y-dir [x y dir-y field-size]
  (let [[field-width field-height] field-size]
    (if (or (<= y (- (/ field-height 2)))
            (>= y (/ field-height 2)))
      (- dir-y)
      dir-y)))

(register-handler
  :tick-ball
  (fn [db _]
    (if-let [ball (.getObjectByName js/scene "my-ball")]
      (let[[x y] (get-in db [:game :entities :ball :pos])
           field-size (get-in db [:game :field-size])
           ball-speed (get-in db [:game :entities :ball :speed])
           [dir-x dir-y] (get-in db [:game :entities :ball :dir])
           dir-y (get-ball-y-dir x y dir-y field-size)
           new-x (+ x ball-speed)
           new-y (+ y (* dir-y ball-speed))]
        (if-not (or (= x new-x) (= y new-y))
          (update-webgl-obj "my-ball" [["position" "x" new-x]
                                       ["position" "y" new-y]]))
        (assoc-in db [:game :entities :ball] {:pos [new-x new-y 0]
                                              :speed ball-speed
                                              :dir [dir-x dir-y]}))
      db)))

(register-handler
  :update-camera
  (fn [db _]
    (let [camera (get-in db [:game :camera])
          player-paddle (get-in db [:game :entitites :player-paddle])
          [player-x player-y player-z] (:pos player-paddle)]
            (dispatch [:update-camera-webgl])
      (assoc-in db [:game :camera :pos] [(+ player-x -300) 0 (+ player-z 100)]))))
