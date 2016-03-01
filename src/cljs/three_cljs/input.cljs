(ns three-cljs.input)

(def key-name
  {65 :a
   87 :w
   68 :d
   83 :s
   80 :p})

(defn- key-down-handler [event]
  (let [keycode (.-keyCode event)
         key-pressed (key-name keycode)]
    (case key-pressed
      :p (js/console.log "p")
      (js/console.log (str "down: " keycode)))))

(defn- key-up-handler [event]
  (let [keycode (.-keyCode event)]
    ()))

(defn add-event-listeners []
  (.addEventListener js/window "keydown" key-down-handler)
  (.addEventListener js/window "keyup" key-up-handler))
