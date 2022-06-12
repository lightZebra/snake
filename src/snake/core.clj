(ns snake.core
  (:require [snake.console :as console]
            [snake.snake :as snake]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import (org.jline.terminal Terminal)))

(defn game-iteration [config state display ^Terminal terminal]
  (display state config)
  (-> state
      (snake/direction (console/input config terminal))
      (snake/tick snake/next-food)))

(defn game-loop [config display terminal]
  (->> (:snake config)
       (iterate #(game-iteration config % display terminal))
       (drop-while snake/alive?)
       (first)))

(defn -main [& _]
  (let [config   (edn/read-string (slurp (io/resource "config.edn")))
        terminal (console/raw-terminal)]
    (doto (game-loop config console/display terminal)
      (console/display config)
      (console/display-score config))))