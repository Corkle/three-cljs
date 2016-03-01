(set-env!
  :source-paths #{"src/cljs"}
  :resource-paths #{"resources/public"}
  :dependencies '[
                   [org.clojure/clojure       "1.8.0"          :scope "provided"]
                   [org.clojure/clojurescript "1.7.228"        :scope "provided"]
                   [adzerk/boot-cljs          "1.7.228-1"      :scope "test"]
                   [pandeiro/boot-http        "0.7.1-SNAPSHOT" :scope "test"]
                   [adzerk/boot-reload        "0.4.4"          :scope "test"]
                   [adzerk/boot-cljs-repl     "0.3.0"          :scope "test"]
                   [com.cemerick/piggieback   "0.2.1"          :scope "test"]
                   [weasel                    "0.7.0"          :scope "test"]
                   [org.clojure/tools.nrepl   "0.2.12"         :scope "test"]
                   [reagent                   "0.6.0-alpha"    :scope "provided"]
                   [re-frame                  "0.7.0-alpha-2"  :scope "provided"]
                   [cljsjs/three "0.0.72-0" :scope "provided"]
                   ])

(require '[adzerk.boot-cljs       :refer [cljs]]
         '[pandeiro.boot-http     :refer [serve]]
         '[adzerk.boot-reload     :refer [reload]]
         '[adzerk.boot-cljs-repl  :refer [cljs-repl start-repl]])

(deftask dev
  "Launch dev environment"
  []
  (comp
    (serve :dir "target")
    (watch)
    (reload)
    (cljs-repl)
    (cljs)
    (target :dir #{"target"})))

(deftask prod
  "Compile production files"
  []
  (comp
    (cljs :optimizations :advanced
          :compiler-options {:externs ["externs/three.ext.js"]})
    (target :dir #{"target"})))
