(ns hanoi.core
  (:use [hanoi.repl :only [connect]]
        [hanoi.stack :only [sched simulation-loop]])
  (:require [crate.core :as crate]))


; fire up a repl for the browser and eval namespace on top once connected
#_(do (ns hanoi.clojure.start)
      (require 'cljs.repl.browser)
      (cemerick.piggieback/cljs-repl
       :repl-env (doto (cljs.repl.browser/repl-env :port 9009)
                   cljs.repl/-setup)))


; ui
(defn draw-state! [key ref old new]
  (doall (map #(set! (.-innerHTML (.getElementById js/document (name %)))
                     (apply str (map (fn [e]
                                       (.-outerHTML (crate/html [:div {:style {:width (str (* 20 e) "px")
                                                                               :height "10px"
                                                                               :margin "0 auto"
                                                                               :background-color "red"}}])))
                                     (% new))))
              (keys new))))


; state management
(def some-towers (atom {}))

(defn init []
  (swap! some-towers (fn [] {:A '(1 2 3 4 5 6)
                            :B '()
                            :C '()})))

(defn move-plate! [towers from to]
  (swap! towers #(assoc % to (cons (first (from %)) (to %))
                        from (rest (from %)))))

(add-watch some-towers :draw draw-state!)


; algorithm using hanoi.stack delayed scheduling
(defn thanoi [height towers A B C]
  (if (== height 1)
    (sched #(move-plate! towers A C))
    (sched #(thanoi (dec height) towers A C B)
           #(move-plate! towers A C)
           #(thanoi (dec height) towers B A C))))


#_(set! (.-innerHTML (.getElementById js/document "A")) "<h1>Salut</h1>")


#_(init)
#_(simulation-loop #(thanoi (count (:A @some-towers)) some-towers :A :B :C))
