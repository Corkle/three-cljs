(ns three-cljs.debug
  (:require [cljs.pprint :refer [pprint]]
            [reagent.core :as r]))

(declare debug-app-state)

(defn- mount-debug []
  (r/render #'debug-app-state (.getElementById js/document "debug")))

(defn- mount-app-state [get-in-vec]
  (r/render-component [:pre (with-out-str (pprint (get-in @re-frame.db/app-db get-in-vec)))] (.getElementById js/document "app-state")))

(defn- debug-button
  [get-in-vec text]
  [:button {:type "button"
             :on-click #(mount-app-state get-in-vec)}
    text])

(defn- close-debug []
  (r/render-component [:button {:title "Debug app-state"
                                :type "button"
                                :on-click #(mount-debug)
                                :style {:position "fixed"
                                        :bottom 0
                                        :right 0
                                        :margin "5px"}}
                       "+"]
                      (.getElementById js/document "debug")))

(defn debug-app-state []
  [:div {:style {:background-color "#ffcccc"}}
   [:hr]
   [:div [:button {:title "Hide Debugger"
                   :type "button"
                   :on-click #(close-debug) :style {:position "fixed"
                                                    :bottom 0
                                                    :right 0
                                                    :margin "5px"}}
          "X"]]
   [debug-button [] "App-State"]
   [debug-button [:config] "Config"]
   [debug-button [:game :entities] "Entities"]
   [:div#app-state]])

(close-debug)
