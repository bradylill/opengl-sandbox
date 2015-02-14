(ns opengl-sandbox.core
  (:require [opengl-sandbox.tut1 :as tut1]
            [opengl-sandbox.tut2 :as tut2]
            [opengl-sandbox.tut3 :as tut3]))

(def tutorials {:tut1 tut1/run
                :tut2 tut2/run
                :tut3 tut3/run})

(defn -main [& args]
   (println "Starting")
   (((keyword (first args)) tutorials)))
