(ns three-cljs.subscriptions
  (:require [re-frame.core :refer [register-sub]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(register-sub
  :threejs-objects
  (fn [db]
    (reaction (:threejs-objects @db))))
