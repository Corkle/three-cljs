(ns three-cljs.webgl.objs
  (:require [re-frame.core :refer [subscribe dispatch]]
            [three-cljs.webgl.meshes :refer [add-webgl-mesh]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn- update-webgl-pos [id [x y z]]
  (let [obj (.getObjectByName js/scene (str id))]
    (aset obj "position" "x" x)
    (aset obj "position" "y" y)
    (aset obj "position" "z" z)
    nil))

(defn- watch-obj [entity]
  (fn [entity]
    (let [id (:id entity)
          pos (:pos entity)]
      (update-webgl-pos id pos))))

(defn- webgl-objs []
  (let [entities (subscribe [:entities])]
    (fn []
      [:div#objs
       [:div "Game Loaded."]
       (for [{:keys [id] :as entity} (vals @entities)]
         ^{:key id} [watch-obj entity])
       ])))
