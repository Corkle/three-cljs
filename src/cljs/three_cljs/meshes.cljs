(ns three-cljs.meshes)

(defn create_sphere
  [radius segments rings [x y z] name options]
  (let [geometry (THREE.SphereGeometry. radius segments rings)
        material (THREE.MeshLambertMaterial. #js {"color" (:color options)})
        sphere (THREE.Mesh. geometry material)]
    (set! (.-name sphere) name)
    sphere))

(defn create_plane
  [width height quality [x y z] name options]
  (let [geometry (THREE.PlaneGeometry. width height quality quality)
        material (THREE.MeshLambertMaterial. #js {:color (:color options)})
        plane (THREE.Mesh. geometry material)]
    (set! (.-x plane.position) x)
    (set! (.-y plane.position) y)
    (set! (.-z plane.position) z)
    plane))

(defn create_paddle
  [width height depth quality [x y z] name options]
  (let [geometry (THREE.CubeGeometry. width height depth quality quality quality)
        material (THREE.MeshLambertMaterial. #js {"color" (:color options)})
        paddle (THREE.Mesh. geometry material)]
    (set! (.-x paddle.position) x)
    (set! (.-y paddle.position) y)
    (set! (.-z paddle.position) z)
    paddle))
