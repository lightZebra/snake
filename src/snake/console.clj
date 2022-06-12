(ns snake.console
  (:require [snake.snake :as snake])
  (:import (org.jline.terminal TerminalBuilder Terminal)))

(def clear-sequence "\033[H\033[2J")

(defn display [{{:keys [display-mapping]} :console}
               {:keys [height weight] :as state}]
  (let [sb (StringBuilder.)]
    (dotimes [row height]
      (dotimes [col weight]
        (.append sb (display-mapping (snake/point-type state [row col]))))
      (.append sb "\r\n"))
    (println clear-sequence)
    (println (str sb))))

(defn display-score [{{:keys [final-message]} :console}
                     state]
  (println (str final-message (snake/score state))))

(defn raw-terminal []
  (doto (TerminalBuilder/terminal)
    (.enterRawMode)))

(defn input [{:keys [timeout console]}
             ^Terminal terminal]
  (let [read (.read (.reader terminal) ^int timeout)]
    (when (not= -2 read)
      (get-in console [:input-mapping (char read)]))))
