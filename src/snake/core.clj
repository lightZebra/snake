(ns snake.core
  (:require [snake.console :as console]
            [snake.snake :as snake]
            [snake.bot :as bot]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def mode
  {:interactive console/terminal-input
   :bot         bot/cycle-input})

(defn game-iteration [config state display input]
  (display state config)
  (-> state
      (snake/direction (input state config))
      (snake/tick snake/next-food)))

(defn game-loop [config display input]
  (->> (:snake config)
       (iterate #(game-iteration config % display input))
       (drop-while snake/alive?)
       (first)))

(defn -main [& [arg]]
  (let [config (edn/read-string (slurp (io/resource "config.edn")))
        input  ((mode (keyword (or arg :interactive))))]
    (doto (game-loop config console/display input)
      (console/display config)
      (console/display-score config))))

(comment
  (-main :bot))