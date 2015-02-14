(ns opengl-sandbox.core
  (:require [opengl-sandbox.basic.tut1 :as tut1]
            [opengl-sandbox.basic.tut2 :as tut2]
            [opengl-sandbox.basic.tut3 :as tut3]
            [opengl-sandbox.basic.tut4 :as tut4]))

(def tutorials {:tut1 tut1/run
                :tut2 tut2/run
                :tut3 tut3/run
                :tut4 tut4/run})

(defn -main [& args]
   (println "Starting")
   (((keyword (first args)) tutorials)))
