(ns three-cljs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [three-cljs.draw :refer [draw]]
            [three-cljs.entities.core :refer [tick]]
            [three-cljs.entities.camera :refer [create-camera]]
            [three-cljs.entities.light :refer [create-light]]
            [three-cljs.entities.ball :refer [create-ball]]
            [three-cljs.entities.game-field :refer [create-field]]
            [three-cljs.entities.player-paddle :refer [create-player-paddle move-player-paddle]]
            [three-cljs.entities.opponent-paddle :refer [create-opponent-paddle]]))

(defn- populate-game-entities [[f-width f-height]]
  (let [ball (conj (create-ball) {:speed 2 :dir [1 1]})
        player (create-player-paddle [(+ (/ f-width -2) 10) 0 12])
        opponent (create-opponent-paddle [(+ (/ f-width 2) -10) 0 12])]
    {:ball ball
     :player-paddle player
     :opponent-paddle opponent}))

(register-handler
  :initialize-db
  (fn [_ _]
    (let [canvas-size [640 360]
          [WIDTH HEIGHT] canvas-size
          camera (create-camera :game-camera :perspective [50 (/ WIDTH HEIGHT) 0.1 10000] [0 0 320])
          main-light (create-light :main-light :point 0xF8D898 [2.5 10000] [500 600 900])
          spot-light (create-light :spot-light :spot 0xF8D898 [1.5 true] [0 0 0])
          field-size [400 200]
          field (create-field [(* .95 400) 200])
          entities (populate-game-entities field-size)]
      {:config {:canvas-size canvas-size
                :canvas-div-id "gameCanvas"}
       :webgl {}
       :game {:running? true
              :camera camera
              :world {:lights {
;;                                 :main main-light
                               :spot-light spot-light}
                      :field {:size field-size
                              :plane field}
                      :entities entities}}})))

(register-handler
  :webgl-scene-ready
  (fn [db _]
    (assoc-in db [:webgl :scene-initialized?] true)))

(register-handler
  :webgl-objs-ready
  (fn [db _]
    (assoc-in db [:webgl :objs-initialized?] true)))

;; --------- Game Logic

(register-handler
  :pause-game
  (fn [db _]
    (update-in db [:game :running?] not)))

;; --------- WegGL Events

(register-handler
  :draw-scene
  (fn [db _]
    (draw)
    db))

(register-handler
  :set-camera
  (fn [db _]
    (let [[player-x _ player-z] (get-in db [:game :world :entities :player-paddle :pos])
          camera (get-in db [:game :camera])
          camera-rotation [0 (* -60 (/ (.-PI js/Math) 180)) (* -90 (/ (.-PI js/Math) 180))]]
      (assoc-in db [:game :camera]
                (-> camera
                    (assoc-in [:pos] [(+ player-x -100) 0 (+ player-z 100)])
                    (assoc-in [:rot] camera-rotation))))))

;; --------- Entity Events

(defn- tick-entity
  [world entity]
  (tick entity world))

(defn- tick-all
  [world]
  (reduce tick-entity world (vals (:entities world))))

(register-handler
  :tick
  (fn [db _]
    (update-in db [:game :world] tick-all)))

(register-handler
  :move-player
  (fn [db [_ dir]]
    (let [world (get-in db [:game :world])]
      (update-in db [:game :world] move-player-paddle dir))))
