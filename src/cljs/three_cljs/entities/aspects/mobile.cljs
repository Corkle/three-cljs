(ns three-cljs.entities.aspects.mobile)

(defprotocol Mobile
  (move [this world dest]
        "Move this entity to a new position.")
  (can-move? [this world dir]
             "Return whether the entity can move to the new position."))
