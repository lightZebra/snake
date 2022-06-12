(ns snake.console
  (:require [snake.snake :as snake])
  (:import (org.jline.terminal TerminalBuilder)
           (clojure.lang IDeref)
           (java.io Closeable)))

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

(defn raw-terminal []
  (doto (TerminalBuilder/terminal)
    (.enterRawMode)))

(defn input-atom [terminal]
  (let [reader  (.reader terminal)
        running (atom true)
        place   (atom nil)]
    (future
      (while @running
        (reset! place (char (.read reader)))))
    (reify
      IDeref
      (deref [_] @place)
      Closeable
      (close [_] (reset! running false)))))

