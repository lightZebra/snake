(ns snake.core
  (:require [snake.console :as console]
            [snake.snake :as snake]))

(def snake
  {:height     10
   :weight     15
   :points     '([1 1])
   :dir        :down
   :food-point [3 1]})

(defn game-iteration [state display input]
  (let [input* @input]
    (display state)
    (Thread/sleep 300)
    (snake/tick (snake/direction state input*))))

(defn game-loop [state display input]
  (->> state
       (iterate #(game-iteration % display input))
       (drop-while snake/alive?)
       (first)))

(defn -main [& _]
  (let [[running input] (console/input-thread)
        end-state (game-loop snake console/display input)]
    (prn "Game over, score: " (snake/score end-state))
    (reset! running false)))