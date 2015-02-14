(ns opengl-sandbox.tut2
   (:import [org.lwjgl.opengl DisplayMode Display]
            [org.lwjgl.input Keyboard Mouse]))

(defn poll-mouse []
  (let [mx (Mouse/getX)
        my (Mouse/getY)
        mouse-click? (Mouse/isButtonDown 0)]
    (if mouse-click?
      (println "Mouse down @ x:" mx " y:" my))))

(defn poll-keyboard []
  (while (Keyboard/next)
    (if (Keyboard/getEventKeyState)
      (cond
        (= (Keyboard/getEventKey) (Keyboard/KEY_A)) (println "A Key Pressed")
        (= (Keyboard/getEventKey) (Keyboard/KEY_S)) (println "S Key Pressed")
        (= (Keyboard/getEventKey) (Keyboard/KEY_D)) (println "D Key Pressed"))
      (cond
        (= (Keyboard/getEventKey) (Keyboard/KEY_A)) (println "A Key Released")
        (= (Keyboard/getEventKey) (Keyboard/KEY_S)) (println "S Key Released")
        (= (Keyboard/getEventKey) (Keyboard/KEY_D)) (println "D Key Released")))))

(defn poll-input []
  (poll-mouse)
  (poll-keyboard))

(defn game-loop []
  (while (not (Display/isCloseRequested))
    (poll-input)
    (Display/update)))

(defn run []
  (println "Running tutorial 2")
  (Display/setDisplayMode (DisplayMode. 800 600))
  (Display/setTitle "Tutorial 2")
  (Display/create)
  (game-loop))
