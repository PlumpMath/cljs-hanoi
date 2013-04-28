(ns hanoi.stack)


(def stack (atom '()))


(defn sched
  "Schedule any number of functions next on the stack (in reverse order ofc.)."
  [& fns]
  (doall (map #(swap! stack (partial cons %)) (reverse fns))))


(defn simulation-loop
  ([start-fn] (sched start-fn)
     (simulation-loop))
  ([] (if-let [f (first @stack)]
        (do
          (.log js/console (str @stack))
          (swap! stack rest)
          (f)
          (js/setTimeout simulation-loop 1000)))))
