(ns opengl-sandbox.tut4
   (:import [org.lwjgl.opengl DisplayMode Display GL11]
            [org.lwjgl.input Keyboard]
            [org.lwjgl Sys]))

(def screen-width 800)
(def screen-height 600)
(def init-quad {:quad-x 400.0 :quad-y 300.0 :quad-rot 0.0})

(defn draw-quad [{:keys [quad-x quad-y quad-rot]}]
  (GL11/glClear (bit-or (GL11/GL_COLOR_BUFFER_BIT) (GL11/GL_DEPTH_BUFFER_BIT)))
  (GL11/glColor3f 0.5 0.5 1.0)
  (GL11/glPushMatrix)
  (GL11/glTranslatef quad-x quad-y 0)
  (GL11/glRotatef quad-rot, 0, 0, 1)
  (GL11/glTranslatef (-' quad-x) (-' quad-y) 0)
  (GL11/glBegin (GL11/GL_QUADS))
  (GL11/glVertex2f (- quad-x 50) (- quad-y 50))
  (GL11/glVertex2f (+ quad-x 50) (- quad-y 50))
  (GL11/glVertex2f (+ quad-x 50) (+ quad-y 50))
  (GL11/glVertex2f (- quad-x 50) (+ quad-y 50))
  (GL11/glEnd)
  (GL11/glPopMatrix))

(defn get-time-in-ms []
  (/ (* (Sys/getTime) 1000) (Sys/getTimerResolution)))

(defn calculate-ms-per-frame [last-time]
  (- (get-time-in-ms) last-time))

(defn movement-horizontal [x]
  (cond
    (Keyboard/isKeyDown (Keyboard/KEY_LEFT)) (- x 0.35)
    (Keyboard/isKeyDown (Keyboard/KEY_RIGHT)) (+ x 0.35)
    :else x))

(defn movement-vertical [y]
  (cond
    (Keyboard/isKeyDown (Keyboard/KEY_UP)) (+ y 0.35)
    (Keyboard/isKeyDown (Keyboard/KEY_DOWN)) (- y 0.35)
    :else y))

(defn update-quad [quad]
 (-> quad
     (update-in [:quad-rot] #(+ % 0.15))
     (update-in [:quad-x] #(movement-horizontal %))
     (update-in [:quad-y] #(movement-vertical %))))

(defn game-loop []
  (loop [running (not (Display/isCloseRequested))
         quad init-quad]
    (let [last-time (get-time-in-ms)
          quad (update-quad quad)]
      (draw-quad quad)
      (Display/update)
      (Display/sync 60)
      (let [ms-per-frame  (calculate-ms-per-frame last-time)
            fps           (/ 1 (/ ms-per-frame 1000.0))]
        (Display/setTitle (format "MS/F: %d FPS: %.2f" ms-per-frame fps)))
      (if running
        (recur (not (Display/isCloseRequested))
               quad)))))

(defn init-opengl []
  (GL11/glMatrixMode (GL11/GL_PROJECTION))
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 screen-width 0 screen-height 1 -1)
  (GL11/glMatrixMode (GL11/GL_MODELVIEW)))

(defn create-display []
  (Display/setDisplayMode (DisplayMode. screen-width screen-height))
  (Display/setTitle "Tutorial 4")
  (Display/create))

(defn run []
  (println "Running tutorial 4")
  (create-display)
  (init-opengl)
  (game-loop))
