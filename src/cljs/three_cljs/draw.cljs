(ns three-cljs.draw)

(defn render []
  (let [cube (.getObjectByName js/SCENE "cube")
        cube-rot-x (.-x (.-rotation cube))
        cube-rot-y (.-y (.-rotation cube))
        cube-rot-z (.-z (.-rotation cube))
        light (.getObjectByName js/SCENE "spotlight")]

    (aset cube "rotation" "x" (+ cube-rot-x (.-rotationX js/guiControls)))
    (aset cube "rotation" "y" (+ cube-rot-y (.-rotationY js/guiControls)))
    (aset cube "rotation" "z" (+ cube-rot-z (.-rotationZ js/guiControls)))

    (aset light "position" "x" (.-lightX js/guiControls))
    (aset light "position" "y" (.-lightY js/guiControls))
    (aset light "position" "z" (.-lightZ js/guiControls))
    nil))

(defn draw []
  ;;   loop the draw() function
  (js/requestAnimationFrame draw)
  (render)
  (.update js/stats)

  ;;   draw THREE.JS scene
  (. js/RENDERER (render js/SCENE js/CAMERA)))

