(ns three-cljs.input)

(def key-name
  {65 :a
   87 :w
   68 :d
   83 :s})

(defn- key-down-handler [event]
  (let [keycode (key-name (.-keyCode event))]
    (case keycode
      :s (js/console.log js/scene)
      (js/console.log (str "down: " keycode)))))

(defn- key-up-handler [event]
  (let [keycode (.-keyCode event)]
    (js/console.log (str "up: "(key-name keycode)))))

(defn add-event-listeners []
  (.addEventListener js/window "keydown" key-down-handler)
  (.addEventListener js/window "keyup" key-up-handler))
