(ns three-cljs.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [three-cljs.handlers]
            [three-cljs.subscriptions]
            [three-cljs.debug]
            [three-cljs.input :refer [add-event-listeners]]))


(defn ^:export init
  []
  (let [c (.getElementById js/document "gameCanvas")]
    (rf/dispatch [:initialize-db])
    (rf/dispatch [:initialize-scene])
    (add-event-listeners)
    ))
