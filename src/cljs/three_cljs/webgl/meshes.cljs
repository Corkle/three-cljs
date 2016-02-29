(ns three-cljs.webgl.meshes)

(defmulti add-webgl-mesh
  (fn [entity] (:ui-type entity)))

(defmethod add-webgl-mesh :sphere
  [{:keys [id material size pos]}]
  (let [[radius w-segments h-segments] size
        color (:color material)
        [x y z] pos
        g (THREE.SphereGeometry. radius w-segments h-segments)
        m (THREE.MeshLambertMaterial. #js {"color" color})
        sphere (THREE.Mesh. g m)]
    (aset sphere "name" (str id))
    (aset sphere "position" "x" x)
    (aset sphere "position" "y" y)
    (aset sphere "position" "z" z)
    (aset sphere "castShadow" true)
    (.add js/scene sphere)))

(defmethod add-webgl-mesh :plane
  [{:keys [id material size]}]
  (let [[width height] size
        color (:color material)
        g (THREE.PlaneGeometry. width height 10)
        m (THREE.MeshLambertMaterial. #js {:color color})
        plane (THREE.Mesh. g m)]
    (aset plane "name" (str id))
    (aset plane "receiveShadow" true)
    (.add js/scene plane)))

(defmethod add-webgl-mesh :box
  [{:keys [id material size pos]}]
  (let [[width height depth] size
        color (:color material)
        [x y z] pos
        g (THREE.BoxGeometry. width height depth)
        m (THREE.MeshLambertMaterial. #js {"color" color})
        box (THREE.Mesh. g m)]
    (aset box "name" (str id))
    (aset box "position" "x" x)
    (aset box "position" "y" y)
    (aset box "position" "z" z)
    (.add js/scene box)))
