(ns three-cljs.webgl.lights)

(defmulti add-webgl-light
  (fn [light] (:type light)))

(defmethod add-webgl-light :point
  [{:keys [id hex props pos]}]
  (let [[intensity distance] props
        [x y z] pos
        point-light (THREE.PointLight. hex intensity distance)]
    (aset point-light "name" (str id))
    (aset point-light "position" "x" x)
    (aset point-light "position" "y" y)
    (aset point-light "position" "z" z)
    (.add js/scene point-light)))

(defmethod add-webgl-light :spot
  [{:keys [id hex props pos]}]
  (let [[intensity cast-shadow?] props
        [x y z] pos
        spot-light (THREE.SpotLight. hex intensity)
        helper (THREE.SpotLightHelper. spot-light)]
    (aset spot-light "name" (str id))
    (aset spot-light "position" "x" x)
    (aset spot-light "position" "y" y)
    (aset spot-light "position" "z" z)
    (aset spot-light "castShadow" true)
    (aset spot-light "shadowCameraVisible" true)
    (.add js/scene helper)
    (.add js/scene spot-light)))
