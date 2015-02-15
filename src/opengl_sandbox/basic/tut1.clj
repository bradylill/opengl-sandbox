(ns opengl-sandbox.basic.tut1
   (:import [org.lwjgl.opengl DisplayMode Display PixelFormat ContextAttribs GL11]))

(defn game-loop []
  (while (not (Display/isCloseRequested))
   (Display/update)))

(defn run []
  (println "Running tutorial 1")
  (Display/setDisplayMode (DisplayMode. 800 600))
  (Display/setTitle "Tutorial 1")
  (Display/create (PixelFormat.) (-> (ContextAttribs. 3 2)
                                     (.withForwardCompatible true)
                                     (.withProfileCore true)))
  (println "OpenGL version: "  (GL11/glGetString GL11/GL_VERSION))
  (println "OpenGL renderer: " (GL11/glGetString GL11/GL_RENDERER))
  (game-loop))
