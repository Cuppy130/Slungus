package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import engine.*;
import game.Game;

public class Main {
    private boolean showWarning = false;
    private long timerBegin;
    private long timerCurrent;
    private int warningLength = 5000;
    public static long window;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private Slungus slungus;
    public static boolean isBattling = false;
    public static AudioManager AM;
    public static int slungusTexture;
    public static Model slungusRenderer;
    private Game game;
    public static int red;

    // Delta time
    private long frameTimeA = System.currentTimeMillis();
    private long frameTimeB = System.currentTimeMillis();
    public float delta = 0;

    private Model warningImage;
    private Model slungusBackgroundModel;
    public void delta(){
        frameTimeB = frameTimeA;
        frameTimeA = System.currentTimeMillis();
        delta = (float) (frameTimeA - frameTimeB) / 60;
    }
    public static void main(String[] args) {
        new Main().run();
    }
    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        window = glfwCreateWindow(WIDTH, HEIGHT, "Click slungus to win!", 0, 0);
        if (window == 0) {
            glfwTerminate();
            throw new RuntimeException("Failed to create GLFW window");
        }
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glViewport(0, 0, WIDTH, HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - WIDTH) / 2, (videoMode.height() - HEIGHT) / 2);
        glfwSetWindowSize(window, WIDTH, HEIGHT);
        glfwSetWindowAttrib(window, GLFW_RESIZABLE, GLFW_FALSE);
        slungusTexture = TextureUtils.loadTexture("/res/Slungus.png");
        red = TextureUtils.loadTexture("/res/red.png");
        slungus = new Slungus();
        AM = new AudioManager();
        AM.init();

        game = new Game();

        warningImage = new Model(new float[]{0, 0, 640, 0, 0, 480, 640, 480}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, TextureUtils.loadTexture("/res/warning.png"));
        slungusBackgroundModel = new Model(new float[]{0, 0, 640, 0, 0, 480, 640, 480}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, slungusTexture);

        AM.addAudio("congratulations", "/res/coratulations.ogg");

        glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
            System.out.println("click");
            if(!isBattling){
                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    double[] xpos = new double[1];
                    double[] ypos = new double[1];
                    GLFW.glfwGetCursorPos(windowHandle, xpos, ypos);
                    double x = xpos[0]; double y = ypos[0];
                    System.out.println(ypos[0]);
                    if(x>135&&y>15&&x<219&&y<62){
                        isBattling = true;
                        glfwSetMouseButtonCallback(windowHandle, null);
                        startBattle();
                    } else if(ypos[0] > 78){
                        AM.stop("congratulations");
                        AM.playSound("congratulations", null);
                        WinScreen.show();
                    }
                }
            }
        });
    }

    public void startBattle() {
        showWarning = true;
        timerBegin = System.currentTimeMillis();
        isBattling = true;

        game.setup();

        glfwSetWindowTitle(Main.window, "SURVIVVE.");
        AM.addAudio("intro", "/res/intro.ogg");
        AM.addAudio("loop", "/res/loop.ogg");
        AM.addAudio("lazer", "/res/lazer.ogg");
        AM.addAudio("alarm", "/res/alarm.ogg");
        AM.addAudio("note", "/res/endnote.ogg");
        AM.addAudio("hurt", "/res/hurt.ogg");
        AM.addAudio("hurt2", "/res/hurt.ogg");
        AM.addAudio("glass", "/res/glassbreakingsfx.ogg");
        AM.addAudio("slungus", "/res/this slungus to hold me.ogg");

        AM.setVolume("lazer", 0.2f);
        AM.setVolume("loop", 0.2f);
        AM.setVolume("intro", 0.2f);
        AM.setVolume("alarm", 0.2f);
        AM.setVolume("note", 0.2f);
        AM.setVolume("hurt2", 0.5f);
        AM.setPitch("hurt2", 3);

        AM.setLoop("loop", true);

        AM.playSound("note", null);
        AM.playSound("alarm", ()-> {
            AM.playSound("intro", () -> {
                AM.playSound("loop", null);
            });
        });
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            Main.AM.update();
            glClear(GL_COLOR_BUFFER_BIT);
            RenderTaskQueue.processTasks();

            glColor4f(.25f, .25f, .25f, 1f);
            slungusBackgroundModel.render();
            glColor4f(1f, 1f, 1f, 1f);

            if(!isBattling){
                delta();
                slungus.draw();
            } else {
                game.update(delta);
                game.draw();
            }

            if(showWarning){
                timerCurrent = System.currentTimeMillis();
                float linear = ((timerCurrent - timerBegin) % 1250) / 1250f;
                float alpha = (linear < 0.5) ? linear * 2 : (1 - linear) * 2;
                glColor4f(1f, 1f, 1f, alpha);
                warningImage.render();
                if(timerCurrent - timerBegin>warningLength){
                    showWarning = false;
                }
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glfwDestroyWindow(window);
        AM.cleanup();
        warningImage.cleanup();
        slungus.cleanup();
        slungusBackgroundModel.cleanup();
        
        glfwTerminate();
        System.exit(0);
    }
    public static void closeWindow() {
        glfwDestroyWindow(window);
    }
}