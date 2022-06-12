(ns snake.bot
  (:require [snake.snake :as snake]))

(defn cycle-input []
  (let [cycle (atom 0)]
    (fn [{:keys [height weight] :as state}
         {{:keys [timeout]} :bot}]
      (Thread/sleep timeout)
      (let [[row col :as head] (snake/head state)
            last-row (dec height)
            last-col (dec weight)]
        (cond
          (= [0 0] head) :down
          (= [last-row 0] head) :right
          (= [last-row last-col] head) :up
          (= [0 last-col] head) :left

          (and (= last-row row)
               (= (dec last-col) col)
               (odd? weight)
               (odd? (swap! cycle inc))) :up

          (zero? col) :down
          (= last-col col) :up
          (= last-row row) :right

          (and (zero? row) (odd? col)) :left
          (and (zero? row) (even? col)) :down
          (and (= (dec last-row) row) (odd? col)) :up
          (and (= (dec last-row) row) (even? col)) :left)))))
