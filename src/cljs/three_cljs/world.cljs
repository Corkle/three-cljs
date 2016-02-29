(ns three-cljs.world)

(def directions
  {:up [0 1 0]
   :down [0 -1 0]
   :left [-1 0 0]
   :right [1 0 0]})

(defn- offset-pos
  [[x y z] [dx dy dz]]
  [(+ x dx) (+ y dy) (+ z dz)])

(defn destination-pos
  [origin dir speed]
  (let [offset-amount (mapv #(* speed %) (directions dir))]
    (offset-pos origin offset-amount)))

(defn is-inbounds?
  [world [w h d] [x y z]]
  (let [[f-width f-height] (get-in world [:field :size])
        [min-x max-x] [(+ (/ f-width -2) (/ w 2)) (- (/ f-width 2) (/ w 2))]
        [min-y max-y] [(+ (/ f-height -2) (/ h 2)) (- (/ f-height 2) (/ h 2))]]
    (and (>= y min-y) (<= y max-y) (>= x min-x) (<= x max-x))))
