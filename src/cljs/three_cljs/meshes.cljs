(ns three-cljs.meshes)

(defn create_sphere
  [radius segments rings]
  (let [geometry (THREE.SphereGeometry. radius segments rings)
        material (THREE.MeshLambertMaterial. #js {"color" 0xD43001})]
    (THREE.Mesh. geometry material)))

(defn create_plane
  [width height quality]
  (let [geometry (THREE.PlaneGeometry. (* .95 width) height quality quality)
        material (THREE.MeshLambertMaterial. #js {:color 0x4BD121})]
    (THREE.Mesh. geometry material)))

(defn create_paddle
  [width height depth quality color]
  (let [geometry (THREE.CubeGeometry. width height depth quality quality quality)
        material (THREE.MeshLambertMaterial. #js {"color" color})]
    (THREE.Mesh. geometry material)))
