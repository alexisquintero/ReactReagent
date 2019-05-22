(ns tac.Game
  (:require [reagent.core :as r]))

(defn calculate-winner [squares]
  (let [lines [[0, 1, 2]
               [3, 4, 5]
               [6, 7, 8]
               [0, 3, 6]
               [1, 4, 7]
               [2, 5, 8]
               [0, 4, 8]
               [2, 4, 6]]]
    (->> (for [[a b c] lines]
           (if (= (squares a) (squares b) (squares c)) (squares a)))
         (filter some?)
         (first))))

(defn Square [value handle-click]
  [:button.square {:on-click handle-click} value])

(defn Board [squares handle-click]
  (let [render-square (fn [i] [Square (squares i) (partial handle-click i)])]
    (fn []
      [:div
       (for [x (range 3)]
         [:div.board-row
          (for [y (range 3)]
            (render-square (+ (* x 3) y)))])])))

(defn Game []
  (let [state (r/atom {:history (->> (repeat 9 nil)
                                     (vec)
                                     (vector))
                       :x-is-next true
                       :step-number 0})
        handle-click (fn [i] (let [history (->> (:history @state)
                                                (take (inc (:step-number @state)))
                                                (vec))
                                   squares (last history)]
                              (when (not(calculate-winner squares))
                                (let [new-squares (assoc squares i (if (:x-is-next @state) "X" "O"))]
                                  (swap! state assoc
                                          :history (conj history new-squares)
                                          :x-is-next (not (:x-is-next @state))
                                          :step-number (count history))))))
        jump-to (fn [step] (swap! state assoc :step-number step :x-is-next (rem step 2)))]
    (fn []
      (let [current ((:history @state) (:step-number @state))
            winner (calculate-winner current)
            status (if winner
                     (str "Winner: " winner)
                     (str "Next player: " (if (:x-is-next @state) "X" "O")))
            moves (map-indexed (fn [idx itm] (let [desc (if (= 0 idx) "Go to game start" (str "Go to move #" idx))]
                                  [:li {:key idx}
                                   [:button {:on-click (partial jump-to idx)} desc]])) (:history @state))]
        [:div.game
         [:div.game-board [Board current handle-click]]
         [:div.game-info
          [:div status]
          [:ol moves]]]))))

(defn rDOM []
  (r/render [Game]
            (js/document.getElementById "root")))
