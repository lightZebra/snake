(ns snake.console
  (:require [snake.snake :as snake]
            [clojure.string :as str]))

(def clear-sequence "\033[H\033[2J")

(def mapping
  {:head  "h"
   :food  "f"
   :snake "s"
   :empty "."})

(defn display
  ([state]
   (display mapping state))
  ([f {:keys [height weight] :as state}]
   (let [sb (StringBuilder.)]
     (dotimes [row height]
       (dotimes [col weight]
         (.append sb (f (snake/point-type state [row col]))))
       (.append sb "\r\n"))
     (println clear-sequence)
     (println (str sb)))))

(def input-mapping
  {\w :up
   \d :right
   \s :down
   \a :left})

(defn input
  ([]
   (input input-mapping))
  ([f]
   (f (char (.read System/in)))))

(defn setup-single-char-reader []
  (.exec
   (Runtime/getRuntime)
   "sh -c stty -icanon min 1 < /dev/tty"))

(defn input-thread []
  (setup-single-char-reader)
  (let [running (atom true)
        place   (atom nil)]
    (future
      (while @running
        (reset! place (input-mapping (char (.read System/in))))))
    [running place]))

