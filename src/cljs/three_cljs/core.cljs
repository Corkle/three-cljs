(ns three-cljs.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [three-cljs.handlers]
            [three-cljs.subscriptions]
            [three-cljs.debug]
            [three-cljs.input :refer [add-event-listeners]]
            [three-cljs.webgl.scene :refer [webgl-scene]]))

(defn loading-panel []
  (let [db-ready? (subscribe [:db-initialized?])]
    (fn []
        (if-not @db-ready?
          [:div#init-db "Initiazling DB..."]
          [webgl-scene]))))

(defn ^:export init
  []
  (let [c (.getElementById js/document "gameCanvas")]
    (dispatch [:initialize-db])
    (add-event-listeners)
    (r/render [loading-panel] (.getElementById js/document "game"))
    (dispatch [:set-camera])))
