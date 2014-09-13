(ns hackthetower.core
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [chord.client :refer [ws-ch]]
    [cljs.core.async :refer [<! >! put! close!]])
  (:use-macros
    [dommy.macros :only [node sel sel1]]
    [cljs.core.async.macros :only [go]]))

(enable-console-print!)

; (println "Hello world!")

; (dommy/append! (sel1 :body) [:p "Hello, World!"])

; ws://172.16.26.153:9000/gol

; (go
;   (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:3000/ws"))]
;     (if-not error
;       (>! ws-channel "Hello server from client!")
;       (js/console.log "Error:" (pr-str error)))))

(defn square [n] (* n n))

(def world-size 50)
(def tile-size 5)
(def states {:dead 0 :alive 1})
(def statekws {0 :dead 1 :alive})
(def colors {:dead "#aaa" :alive "#000"})
(def empty-world (replicate (square world-size) (states :dead)))

(defn create-tile [state]
  [:div {:style
         {:width tile-size
          :height tile-size
          :background-color (colors (statekws state))
          :display "inline-block"}}])

(dommy/append! (sel1 :body) [:div#world {:style
                                  {:width (* tile-size world-size)
                                   :height (* tile-size world-size)}}
                             (map create-tile empty-world)])

(def tiles (sel "#world div"))

(defn update-tile! [tile state]
  (let [state-kw (statekws state)
        color (colors state-kw)]
    (dommy/set-style! tile :background-color color)))

(defn render-world! [world]
  (doseq [[tile state] (map list tiles world)]
    (update-tile! tile state)))

(render-world! (map #(%1 %2) (cycle [inc identity]) empty-world))
