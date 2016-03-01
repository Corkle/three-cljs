(ns three-cljs.input
  (:require [three-cljs.draw :refer [render]]))

(def key-name
  {65 :a
   87 :w
   68 :d
   83 :s
   80 :p})

(defn- window-resize-handler []
  (let [WIDTH (.-innerWidth js/window)
        HEIGHT (.-innerHeight js/window)]
    (aset js/CAMERA "aspect" (/ WIDTH HEIGHT))
    (.updateProjectionMatrix js/CAMERA)
    (.setSize js/RENDERER WIDTH HEIGHT)))

(defn- key-down-handler [event]
  (let [keycode (.-keyCode event)
         key-pressed (key-name keycode)]
;;     (case key-pressed
;;       :p (js/console.log "p")
;;       (js/console.log (str "down: " keycode)))
    ))

(defn- key-up-handler [event]
  (let [keycode (.-keyCode event)]
    ()))

(defn add-event-listeners []
  (.addEventListener js/window "resize" window-resize-handler)
  (.addEventListener js/window "keydown" key-down-handler)
  (.addEventListener js/window "keyup" key-up-handler)
  (.addEventListener js/controls "change" render))
