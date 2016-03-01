(ns three-cljs.draw)

(defn draw []
    ;;   loop the draw() function
    (js/requestAnimationFrame draw)

    ;;   draw THREE.JS scene
    (. js/RENDERER (render js/SCENE js/CAMERA))
    )
