(ns three-cljs.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [three-cljs.handlers]
            [three-cljs.subscriptions]
            [three-cljs.input :refer [add-event-listeners]]))

(def ballDirX 1)
(def ballDirY 1)
(def ballSpeed 2)


(defn ball-physics []
  (let [ball-y (.-y ball.position)
        ball-x (.-x ball.position)]
    (do
      (if (or (<= ball-y (- (/ field-height 2)))
              (>= ball-y (/ field-height 2)))
        (set! ballDirY (- ballDirY)))
      (set! (.-x ball.position) (+ ball-x (* ballDirX ballSpeed)))
      (set! (.-y ball.position) (+ ball-y (* ballDirY ballSpeed))))))

(defn ^:export init
  []
  (let [c (.getElementById js/document "gameCanvas")]
    (rf/dispatch [:initialize-db])
    (rf/dispatch [:initialize-scene])
    (add-event-listeners)
    ))
