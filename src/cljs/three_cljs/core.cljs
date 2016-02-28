(ns three-cljs.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [three-cljs.handlers]
            [three-cljs.subscriptions]
            [three-cljs.debug]
            [three-cljs.input :refer [add-event-listeners]]
            [three-cljs.webgl.objs :refer [webgl-objs]]))

(defn loading-panel []
  (let [game-ready? (subscribe [:game-initialized?])
        webgl-ready? (subscribe [:webgl-initialized?])]
    (fn []
      (let [ready? (and @game-ready? @webgl-ready?)]
        (if-not ready?
          [:div "Initiazling ..."]
          [webgl-objs])))))

(defn ^:export init
  []
  (let [c (.getElementById js/document "gameCanvas")]
    (dispatch [:initialize-db])
    (dispatch [:initialize-scene])
    (add-event-listeners)
    (r/render [loading-panel] (.getElementById js/document "game"))
    ))
