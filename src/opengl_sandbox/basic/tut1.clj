(ns opengl-sandbox.basic.tut1
   (:import [org.lwjgl.opengl DisplayMode Display]))

(defn game-loop []
  (while (not (Display/isCloseRequested))
   (Display/update)))

(defn run []
  (println "Running tutorial 1")
  (Display/setDisplayMode (DisplayMode. 800 600))
  (Display/setTitle "Tutorial 1")
  (Display/create)
  (game-loop))
