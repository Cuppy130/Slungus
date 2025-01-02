package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;

import java.awt.List;
import java.util.ArrayList;

import org.joml.Vector2f;

import engine.AudioManager;
import engine.Texture;
// import entity.Player;
// import entity.SlungusBoss;
import entity.Player;
import entity.Slungus;

public class Gamestate {
    // Gamestate
    public static byte screen = 0;
    public static boolean finalPhase = false;
    public static boolean goldenSlungus = false;
    public static boolean shopOpen = false;
    public static boolean paused = false;
    public static boolean gameOver = false;
    public static boolean bossDefeated = false;
    public static int score = 0;
    public static int reward = 1;
    public static byte maxFrames = 60; // cap at 60 fps
    public static boolean[] keys = new boolean[GLFW_KEY_LAST];

    // Textures
    public static Texture slungusTexture;
    public static Texture clickToWin;

    // Entities
    public static Player player;
    public static Slungus[] slungus = new Slungus[10]; // 10 slungus max at once

    // Timers
    public static long beginTime = System.currentTimeMillis();
    public static long endTime = System.currentTimeMillis();

    // Audio
    public static AudioManager audio;

    public static void init() {
        audio = new AudioManager();
        audio.init();

        slungusTexture = new Texture("/res/images/Slungus.png");
        clickToWin = new Texture("/res/images/slugnus.png");
        player = new Player(new Vector2f(400, 300));
    }

    public static void update(double delta) {
        audio.update();
        if (screen == 0) {
            
        }
    }

    public static boolean keyPressed(int GLFW_KEY){
        return keys[GLFW_KEY];
    }
}
