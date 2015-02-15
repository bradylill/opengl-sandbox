(ns opengl-sandbox.antons.hello-triangle
  (:import [org.lwjgl BufferUtils]
           [org.lwjgl.opengl Display DisplayMode PixelFormat ContextAttribs
                             GL11 GL15 GL20 GL30]))

(def triangle-points
  (float-array [ 0.0  0.5 0.0
                 0.5 -0.5 0.0
                -0.5 -0.5 0.0 ]))

(def vertex-shader
  (str
    "#version 410\n"
    "in vec3 vp;"
    "void main() {"
    "  gl_Position = vec4 (vp.x, vp.y, vp.z, 1.0);"
    "}"))

(def fragment-shader
  (str
    "#version 410\n"
    "out vec4 frag_colour;"
    "void main() {"
    "  frag_colour = vec4 (0.5, 0.0, 0.5, 1.0);"
    "}"))

(defn compile-shaders []
  (let [vs (GL20/glCreateShader GL20/GL_VERTEX_SHADER)
        fs (GL20/glCreateShader GL20/GL_FRAGMENT_SHADER)]
    (GL20/glShaderSource vs vertex-shader)
    (GL20/glCompileShader vs)
    (GL20/glShaderSource fs fragment-shader)
    (GL20/glCompileShader fs)
    [vs fs]))

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
      (.put data)
      (.flip))
    float-buffer))

(defn clear-screen []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))
  (GL11/glClearColor 0.6 0.6 0.6 1.0))

(defn draw [shader-program vao]
  (clear-screen)
  (GL20/glUseProgram shader-program)
  (GL30/glBindVertexArray vao)
  (GL11/glDrawArrays GL11/GL_TRIANGLES 0 3)
  (GL30/glBindVertexArray 0))

(defn populate-vao []
  (let [vao (GL30/glGenVertexArrays)
        vbo (GL15/glGenBuffers)
        points (create-float-buffer triangle-points)]
    (GL30/glBindVertexArray vao)
    (GL20/glEnableVertexAttribArray 0)

    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER points GL15/GL_STATIC_DRAW)
    (GL20/glVertexAttribPointer 0 3 GL11/GL_FLOAT false 0 0)
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)

    (GL30/glBindVertexArray 0)
    vao))

(defn render-loop []
  (let [vao (populate-vao)
        shader-program (create-shader-program)]
    (while (not (Display/isCloseRequested))
      (Display/update)
      (draw shader-program vao)
      (Display/sync 60))))

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
