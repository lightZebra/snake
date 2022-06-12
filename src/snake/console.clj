(ns snake.console
  (:require [snake.snake :as snake])
  (:import (org.jline.terminal TerminalBuilder Terminal)))

(def clear-sequence "\033[H\033[2J")

(defn display [{:keys [height weight] :as state}
               {{:keys [display-mapping]} :console}]
  (let [sb (StringBuilder.)]
    (dotimes [row height]
      (dotimes [col weight]
        (.append sb (display-mapping (snake/point-type state [row col]))))
      (.append sb "\r\n"))
    (println clear-sequence)
    (println (str sb))))

(defn display-score [state {{:keys [final-message]} :console}]
  (println (str final-message (snake/score state))))

(defn ^Terminal raw-terminal []
  (doto (TerminalBuilder/terminal)
    (.enterRawMode)))

(defn terminal-input
  ([]
   (terminal-input (raw-terminal)))
  ([^Terminal terminal]
   (fn [_ {{:keys [timeout input-mapping]} :console}]
     (let [read (.read (.reader terminal) ^int timeout)]
       (when (not= -2 read)
         (input-mapping (char read)))))))
