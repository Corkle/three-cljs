(ns three-cljs.input)

(defn- key-press-handler [event]
  (let [keycode (.-keyCode event)]
    (js/console.log keycode)))

(defn add-event-listeners []
  (.addEventListener js/window "keydown" key-press-handler))
