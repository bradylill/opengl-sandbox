(ns opengl-sandbox.core
  (:require [opengl-sandbox.tut1 :as tut1]
            [opengl-sandbox.tut2 :as tut2]
            ))

(def tutorials {:tut1 tut1/run
                :tut2 tut2/run})

(defn -main [& args]
   (println "Starting")
   (((keyword (first args)) tutorials)))
