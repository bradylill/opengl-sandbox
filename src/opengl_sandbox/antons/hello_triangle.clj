(ns opengl-sandbox.antons.hello-triangle
  (:require [clojure.java.io :as jio])
  (:import [org.lwjgl BufferUtils]
           [org.lwjgl.input Keyboard]
           [org.lwjgl.opengl Display DisplayMode PixelFormat ContextAttribs
                             GL11 GL15 GL20 GL30]))

(def triangle-points
  [ 0.0  0.5 0.0
    0.5 -0.5 0.0
   -0.5 -0.5 0.0 ])

(def colour-points
  [ 1.0 0.0 0.0
    0.0 1.0 0.0
    0.0 0.0 1.0 ])

(def matrix
  [ 1.0 0.0 0.0 0.0
    0.0 1.0 0.0 0.0
    0.0 0.0 1.0 0.0
    0.5 0.0 0.0 1.0 ])

(def speed 1.0)

(defn load-shader-source [file]
  (-> (jio/resource file)
      (.getPath)
      (slurp)))

(def vertex-shader
  {:src (load-shader-source "shaders/hello_triangle.vert")
   :type GL20/GL_VERTEX_SHADER})

(def fragment-shader
  {:src (load-shader-source "shaders/hello_triangle.frag")
   :type GL20/GL_FRAGMENT_SHADER})

(defn compile-shader [shader-src shader-type]
 (let [id (GL20/glCreateShader shader-type)]
    (GL20/glShaderSource id shader-src)
    (GL20/glCompileShader id)
    id))

(defn compile-shaders []
  (map #(compile-shader (:src %) (:type %))  [vertex-shader fragment-shader]))

(defn create-shader-program []
  (let [shader-program (GL20/glCreateProgram)
        shaders (compile-shaders)]
    (doseq [shader shaders]
      (GL20/glAttachShader shader-program shader))
    (GL20/glLinkProgram shader-program)
    shader-program))

(defn create-float-buffer [data]
  (let [float-buffer (BufferUtils/createFloatBuffer (count data))]
    (doto float-buffer
      (.put (float-array data))
      (.flip))
    float-buffer))

(defn clear-screen []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))
  (GL11/glClearColor 0.6 0.6 0.6 1.0))

(defn draw [shader-program vao update-matrix]
  (clear-screen)
  (GL20/glUseProgram shader-program)
  (-> (GL20/glGetUniformLocation shader-program "update_matrix")
      (GL20/glUniformMatrix4 false update-matrix))
  (GL30/glBindVertexArray vao)
  (GL11/glDrawArrays GL11/GL_TRIANGLES 0 3)
  (GL30/glBindVertexArray 0))

(defn create-vbo [points attrib-index]
  (let [vbo (GL15/glGenBuffers)]
    (GL20/glEnableVertexAttribArray attrib-index)
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER points GL15/GL_STATIC_DRAW)
    (GL20/glVertexAttribPointer attrib-index 3 GL11/GL_FLOAT false 0 0)
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)))

(defn populate-vao [all-points]
  (let [vao (GL30/glGenVertexArrays)]
    (GL30/glBindVertexArray vao)

    (doseq [points all-points]
      (create-vbo points (.indexOf all-points points)))

    (GL30/glBindVertexArray 0)
    vao))

(defn isRunning? []
  (not (or (Display/isCloseRequested)
           (Keyboard/isKeyDown (Keyboard/KEY_Q)))))

(defn render-loop []
  (let [vao (populate-vao (map create-float-buffer [triangle-points colour-points]))
        shader-program (create-shader-program)
        update-matrix (create-float-buffer matrix)]
    (loop [running (isRunning?)]
      (Display/update)
      (draw shader-program vao update-matrix)
      (Display/sync 60)
      (when running
        (recur (isRunning?))))
    (println "Stopping...")))

(defn init-gl []
  (GL11/glEnable GL11/GL_DEPTH_TEST)
  (GL11/glDepthFunc GL11/GL_LESS))

(defn create-display [w h title]
  (Display/setDisplayMode (DisplayMode. w h))
  (Display/setTitle title)
  (Display/create (PixelFormat.) (-> (ContextAttribs. 3 2)
                                     (.withForwardCompatible true)
                                     (.withProfileCore true)))
  (println "OpenGL version: "  (GL11/glGetString GL11/GL_VERSION))
  (println "OpenGL renderer: " (GL11/glGetString GL11/GL_RENDERER)))

(defn run []
  (println "Running anton's hello triangle")
  (create-display 800 600 "antons/hello-triangle")
  (init-gl)
  (render-loop))
