(ns three-cljs.entities.core)

(defprotocol Entity
  (tick [this world]
        "Update the world to handle to passing of a tick for this entity"))
