(ns three-cljs.webgl.cameras)

(defmulti add-webgl-camera
  (fn [camera] (:type camera)))

(defmethod add-webgl-camera :perspective
  [{:keys [id props pos rot]}]
  (let [[view-angle aspect near far] props
        [x y z] pos
        [rot-x rot-y rot-z] rot
        p-camera (THREE.PerspectiveCamera. view-angle aspect near far)
        helper (THREE.CameraHelper. p-camera)]
    (aset p-camera "name" (str id))
    (aset p-camera "position" "x" x)
    (aset p-camera "position" "y" y)
    (aset p-camera "position" "z" z)
    (aset p-camera "rotation" "x" rot-x)
    (aset p-camera "rotation" "y" rot-y)
    (aset p-camera "rotation" "z" rot-z)
;;     (.add js/scene helper)
    (.add js/scene p-camera)))
