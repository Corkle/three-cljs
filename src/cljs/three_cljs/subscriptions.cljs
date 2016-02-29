(ns three-cljs.subscriptions
  (:require [re-frame.core :refer [register-sub]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(register-sub
  :db-initialized?
  (fn [db]
    (reaction (not (empty? @db)))))

(register-sub
  :webgl-scene-initialized?
  (fn [db]
    (reaction (get-in @db [:webgl :scene-initialized?]))))

(register-sub
  :webgl-camera-initialized?
  (fn [db]
    (reaction (not (empty? (get-in @db [:game :camera]))))))

(register-sub
  :game-running?
  (fn [db]
    (get-in @db [:game :running?])))

(register-sub
  :config
  (fn [db]
    (reaction (:config @db))))

(register-sub
  :game
  (fn [db]
    (reaction (:game @db))))

(register-sub
  :game-camera
  (fn [db]
    (reaction (get-in @db [:game :camera]))))

(register-sub
  :lights
  (fn [db]
    (reaction (get-in @db [:game :world :lights]))))

(register-sub
  :entities
  (fn [db]
    (reaction (get-in @db [:game :world :entities]))))



(register-sub
  :ball
  (fn [db]
    (reaction (get-in @db [:game :world :entities :ball]))))

(register-sub
  :player-paddle
  (fn [db]
    (reaction (get-in @db [:game :world :entities :player-paddle]))))

(register-sub
  :opponent-paddle
  (fn [db]
    (reaction (get-in @db [:game :world :entities :opponent-paddle]))))


