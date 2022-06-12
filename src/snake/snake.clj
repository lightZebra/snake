(ns snake.snake)

(def directions
  {:up    [-1 0]
   :right [0 1]
   :down  [1 0]
   :left  [0 -1]})

(def opposite-directions
  {:up    :down
   :right :left
   :down  :up
   :left  :right})

(defrecord SnakeState [height weight points dir food-point])

(def head (comp first :points))

(def score (comp count :points))

(defn eating? [{:keys [food-point] :as state}]
  (= (head state) food-point))

(defn alive? [{:keys [height weight points] :as state}]
  (let [[row col :as head] (head state)]
    (and (<= 0 row (dec height))
         (<= 0 col (dec weight))
         (not-any? #{head} (rest points)))))

(defn point-type [{:keys [points food-point] :as state} point]
  (cond
    (= point (head state)) :head
    (= point food-point) :food
    (some #{point} points) :snake
    :else :empty))

(defn direction [{:keys [dir] :as state} direction]
  (if (or (nil? direction)
          (= (opposite-directions dir) direction))
    state
    (assoc state :dir direction)))

(defn update-tail [state]
  (if (eating? state)
    state
    (update state :points butlast)))

(defn update-head [{:keys [dir] :as state}]
  (let [next-head (map + (head state) (directions dir))]
    (update state :points conj next-head)))

(defn update-food [f state]
  (if-not (eating? state)
    state
    (assoc state :food-point (f state))))

(defn next-food [{:keys [height weight points]}]
  (rand-nth
   (remove
    (set points)
    (for [row (range height)
          col (range weight)]
      [row col]))))

(defn tick [state next-food]
  (when (alive? state)
    (->> state
         update-head
         update-tail
         (update-food next-food))))
