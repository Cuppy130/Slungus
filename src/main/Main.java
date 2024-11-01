package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import engine.AudioManager;
import engine.BattlingSlungus;
import engine.Model;
import engine.Slungus;
import engine.TextureUtils;
import engine.TimerUtils;

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
    private BattlingSlungus battle;
    public static AudioManager AM;
    private long frameTimeA = System.currentTimeMillis();
    private long frameTimeB = System.currentTimeMillis();
    public float delta = 0;
    private Model warningImage;
    private Model slungusBackgrounModel;
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
        slungus = new Slungus();
        AM = new AudioManager();
        battle = new BattlingSlungus();
        AM.init();


        warningImage = new Model(new float[]{0, 0, 640, 0, 0, 480, 640, 480}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, TextureUtils.loadTexture("/res/warning.png"));
        slungusBackgrounModel = new Model(new float[]{0, 0, 640, 0, 0, 480, 640, 480}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, TextureUtils.loadTexture("/res/Slungus.png"));

        glfwSetMouseButtonCallback(window, (windowHandle, button, action, mods) -> {
            if(isBattling){
                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS || button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                    double[] xpos = new double[1];
                    double[] ypos = new double[1];
                    glfwGetCursorPos(windowHandle, xpos, ypos);
                    battle.spawnProjectile(xpos[0], ypos[0]);
                    AM.stop("lazer");
                    AM.playSound("lazer", null);
                }
            } else {
                if (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS) {
                    double[] xpos = new double[1];
                    double[] ypos = new double[1];
                    GLFW.glfwGetCursorPos(windowHandle, xpos, ypos);
                    if(xpos[0] > 134 && ypos[0] > 15 && xpos[0] < 220 && ypos[0] < 62){
                        startBattle();
                    } else if(ypos[0]>78){
                        glfwDestroyWindow(windowHandle);
                        JFrame frame = new JFrame("You WIn!!");
                        frame.setAlwaysOnTop(true);
                        frame.setBackground(Color.cyan);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setSize(new Dimension(300, 100));
                        JButton btn = new JButton("Ok");
                        btn.addActionListener(ActionListener -> {
                            System.exit(0);
                        });
                        frame.add(btn);
                        frame.setVisible(true);
                    }
                }
            }
        });
    }
    public void startBattle() {

        
        TimerUtils.invertval(()->{
            System.out.println("triggered");
            for(int i=0; i < 3; i++){
                TimerUtils.timeout(()->{
                    battle.spawnProjectileEnemy(battle.getX(), battle.getY());
                    System.out.println("Spawned enemy projectile");
                }, i*100);
            }
        }, 1000);

        showWarning = true;
        timerBegin = System.currentTimeMillis();
        battle.setup();
        isBattling = true;
        glfwSetWindowTitle(Main.window, "SURVIVVE.");
        AM.addAudio("intro", "/res/intro.ogg");
        AM.addAudio("loop", "/res/loop.ogg");
        AM.addAudio("lazer", "/res/lazer.ogg");
        AM.addAudio("alarm", "/res/alarm.ogg");
        AM.addAudio("note", "/res/endnote.ogg");
        AM.addAudio("hurt", "/res/hurt.ogg");
        AM.addAudio("hurt2", "/res/hurt.ogg");
        AM.addAudio("glass", "/res/glassbreakingsfx.ogg");

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

            glColor4f(.25f, .25f, .25f, 1f);
            slungusBackgrounModel.render();

            glColor4f(1f, 1f, 1f, 1f);
            if(!isBattling){
                delta();
                slungus.draw();
            } else {
                battle.update(delta);
                battle.draw();
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
        battle.cleanup();
        warningImage.cleanup();
        slungus.cleanup();
        slungusBackgrounModel.cleanup();
        battle.cleanup();
        
        glfwTerminate();
        System.exit(0);
    }
    public static void closeWindow() {
        glfwDestroyWindow(window);
    }
}