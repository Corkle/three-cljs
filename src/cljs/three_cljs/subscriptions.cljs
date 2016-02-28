(ns three-cljs.subscriptions
  (:require [re-frame.core :refer [register-sub]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(register-sub
  :ball
  (fn [db]
    (reaction (get-in @db [:game :entities :ball]))))

(register-sub
  :player-paddle
  (fn [db]
    (reaction (get-in @db [:game :entities :player-paddle]))))

(register-sub
  :opponent-paddle
  (fn [db]
    (reaction (get-in @db [:game :entities :opponent-paddle]))))

(register-sub
  :camera
  (fn [db]
    (reaction (get-in @db [:game :camera]))))

(register-sub
  :game-initialized?
  (fn [db]
    (reaction (not (empty? (get-in @db [:game :entities]))))))

(register-sub
  :webgl-initialized?
  (fn [db]
    (reaction (get-in @db [:webgl :initialized?]))))

(register-sub
  :game-running?
  (fn [db]
    (get-in @db [:game :running?])))
