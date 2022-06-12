(ns snake.core
  (:require [snake.console :as console]
            [snake.snake :as snake]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn game-iteration [config state display input]
  (let [input* (get-in config [:console :input-mapping @input])]
    (display config state)
    (Thread/sleep 300)
    (snake/tick (snake/direction state input*))))

(defn game-loop [config state display input]
  (->> state
       (iterate #(game-iteration config % display input))
       (drop-while snake/alive?)
       (first)))

(defn -main [& _]
  (with-open [input (console/input-atom (console/raw-terminal))]
    (let [config    (edn/read-string (slurp (io/resource "config.edn")))
          snake     (:snake config)
          end-state (game-loop config snake console/display input)]
      (prn "Game over, score: " (snake/score end-state)))))