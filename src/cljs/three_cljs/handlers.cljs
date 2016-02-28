(ns three-cljs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [three-cljs.draw :refer [draw]]
            [three-cljs.meshes :refer [create_plane create_sphere create_paddle]]))

(register-handler
  :initialize-db
  (fn [_ _]
    {:config {:canvas-size [640 360]}
     :game {:field-size [400 200]
            :entities {:ball {:x -40
                              :y -40
                              :ball-y-dir 1
                              :ball-x-dir 1
                              :ball-speed 2}}}}))

(register-handler
  :draw-scene
  (fn [db _]
    (draw)
    db))

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
  :add-to-scene
  (fn [db [_ objs]]
    (doseq [obj objs]
      (.add js/scene obj))
    db))

(register-handler
  :load-game
  (fn [db _]
    (let [[field-width field-height] (get-in db [:game :field-size])
          paddle-width 10
          paddle-height 30
          paddle-depth 10
          ball (create_sphere 5 10 16 [0 0 0] "my-ball" {:color 0xD43001})
          plane (create_plane (* .95 field-width) field-height 10 [0 0 0] "my-plane" {:color 0x4BD121})
          paddle1 (create_paddle paddle-width paddle-height paddle-depth 1 [(+ (/ field-width -2) paddle-width) 0 0] "player-paddle" {:color 0x1B32C0})
          paddle2 (create_paddle paddle-width paddle-height paddle-depth 1 [(+ (/ field-width 2) (- paddle-width)) 0 0] "opponent-paddle" {:color 0xFF4045})]
      (dispatch [:add-to-scene [ball plane paddle1 paddle2]])
      db)))

(defn- update-webgl-obj [obj-name prop-vals]
  (let [obj (.getObjectByName js/scene obj-name)]
    (doseq [prop-val prop-vals]
      (apply (partial aset obj) prop-val))))

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
      (let[ball-x (get-in db [:game :entities :ball :x])
           ball-y (get-in db [:game :entities :ball :y])
           field-size (get-in db [:game :field-size])
           ball-speed (get-in db [:game :entities :ball :ball-speed])
           ball-y-dir (get-in db [:game :entities :ball :ball-y-dir])
           ball-y-dir (get-ball-y-dir ball-x ball-y ball-y-dir field-size)
           new-x (+ ball-x ball-speed)
           new-y (+ ball-y (* ball-y-dir ball-speed))]
        (update-webgl-obj "my-ball" [["position" "x" new-x]
                                     ["position" "y" new-y]])
        (assoc-in db [:game :entities :ball] {:x new-x
                                              :y new-y
                                              :ball-speed ball-speed
                                              :ball-y-dir ball-y-dir
                                              :ball-x-dir 1}))
      db)))
