(ns opengl-sandbox.basic.tut3
   (:import [org.lwjgl.opengl DisplayMode Display GL11]))

(def screen-width 800)
(def screen-height 600)

(defn draw-quad []
  (GL11/glClear (bit-or (GL11/GL_COLOR_BUFFER_BIT) (GL11/GL_DEPTH_BUFFER_BIT)))
  (GL11/glColor3f 0.5 0.5 1.0)
  (GL11/glBegin (GL11/GL_QUADS))
  (GL11/glVertex2f 100 100)
  (GL11/glVertex2f 300 100)
  (GL11/glVertex2f 300 300)
  (GL11/glVertex2f 100 300)
  (GL11/glEnd))

(defn game-loop []
  (while (not (Display/isCloseRequested))
    (draw-quad)
    (Display/update)))

(defn init-opengl []
  (GL11/glMatrixMode (GL11/GL_PROJECTION))
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 screen-width 0 screen-height 1 -1)
  (GL11/glMatrixMode (GL11/GL_MODELVIEW)))

(defn create-display []
  (Display/setDisplayMode (DisplayMode. screen-width screen-height))
  (Display/setTitle "Tutorial 3")
  (Display/create))

(defn run []
  (println "Running tutorial 3")
  (create-display)
  (init-opengl)
  (game-loop))
