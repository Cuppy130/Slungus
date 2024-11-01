package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BattlingSlungus {
    private Model slungus;
    private Model player;
    private Model bullet;
    private float x = 640/2, y = -128;
    private float px = 640/2, py = 480/4*3;
    private float slungusSpeed = 1;
    private float speed = 1f;
    private int slungusHealth = 50000;
    private int bulletDamage = 3;
    private int playerHealth = 1000;

    private BossBar bar = new BossBar(slungusHealth, 640-220, 480-40, 200, 20);
    private BossBar playerBar = new BossBar(playerHealth, 20, 480-40, 200, 20);

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private float delta;

    private Model vignette;
    private boolean goldenSlungus = false;

    private long timeLastHit = 0L;
    private int timeDamage = 200;
    private long timeCurrent = System.currentTimeMillis();

    private List<Projectile> projectiles = new CopyOnWriteArrayList<>();
    public BattlingSlungus(){
        slungus = new Model(new float[]{0, 0, 256, 0, 0, 256, 256, 256}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, Main.slungusTexture);
        player = new Model(new float[]{0, 0, 32, 0, 0, 32, 32, 32}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, Main.slungusTexture);
        bullet = new Model(new float[]{0, 0, 16, 0, 0, 16, 16, 16}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, Main.slungusTexture);
        vignette = new Model(new float[]{0, 0, 640, 0, 0, 480, 640, 480}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, TextureUtils.loadTexture("/res/vignette.png"));
    }
    
    public void draw() {
        glColor4f(1, 1, 1, 1);
        bar.render();
        playerBar.render();
        glColor4f(1, 1, 1, 1);
        if(goldenSlungus){
            glColor4f(1, 1, 0, 1);
        }
        slungus.render(x - 128, y - 128);
        if(goldenSlungus){
            glColor4f(1, 1, 1, 1);
        }
        player.render(px - 16, py - 16);

        for (Projectile projectile : projectiles) {
            if (projectile != null) {
                projectile.update();
                projectile.render(() -> bullet.render(projectile.getX() - 8, projectile.getY() - 8));
                if (projectile.shouldBeRemoved()) {
                    projectiles.remove(projectile);
                }
            }
        }
    }
    
    public void cleanup() {
        bar.cleanup();
        player.cleanup();
        slungus.cleanup();
        bullet.cleanup();
        playerBar.cleanup();
    }
    
    public void setup(){
        glfwSetKeyCallback(Main.window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                if(key==GLFW_KEY_W){
                    up = true;
                }
                if(key==GLFW_KEY_S){
                    down = true;
                }
                if(key==GLFW_KEY_A){
                    left = true;
                }
                if(key==GLFW_KEY_D){
                    right = true;
                }
            }
    
            if (action == GLFW_RELEASE) {
                if(key==GLFW_KEY_W){
                    up = false;
                }
                if(key==GLFW_KEY_S){
                    down = false;
                }
                if(key==GLFW_KEY_A){
                    left = false;
                }
                if(key==GLFW_KEY_D){
                    right = false;
                }
            }
        });
    }

    public void setup(boolean goldenSlungus) {
        this.goldenSlungus = goldenSlungus;
        setup();
    }

    public static void closeWindow() {
        glfwSetWindowShouldClose(Main.window, true);
    }

    public void update(float delta) {
        this.timeCurrent = System.currentTimeMillis();
        this.delta = delta;
        bar.updateHealth(slungusHealth);
        playerBar.updateHealth(playerHealth);
        if(slungusHealth<=0){
            return;
        }
        int mxspd = 10;
        if(up){
            py-=speed * delta * mxspd;
        }
        if(down){
            py+=speed * delta * mxspd;
        }
        if(left){
            px-=speed * delta * mxspd;
        }
        if(right){
            px+=speed * delta * mxspd;
        }

        if(px < 16){
            px = 16;
        } else if(px > 624){
            px = 624;
        }
        if(py < 16){
            py = 16;
        } else if(py > 464){
            py = 464;
        }

        float theta = (float) Math.atan2(py - y, px - x);
        x += Math.cos(theta) * slungusSpeed * delta;
        y += Math.sin(theta) * slungusSpeed * delta;

        if(collidingWithSlungus()){
            playerHealth -= 1;
            Main.AM.playSound("hurt", null);
            timeLastHit = System.currentTimeMillis();
        }

        if (timeCurrent - timeLastHit < timeDamage) {
            float alpha = 1 - (float) (timeCurrent - timeLastHit) / timeDamage;
            glColor4f(1, 1, 1, alpha);
            vignette.render();
            glColor4f(1, 1, 1, 1);
        }

        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            if(projectile.AABB(x-128, y-128, 256, 256)&&!projectile.isEnemy()){
                slungusHealth-=bulletDamage;
                Main.AM.playSound("hurt2", null);
            } else if(projectile.AABB(px-16, py-16, 32, 32)&&projectile.isEnemy()){
                playerHealth-=bulletDamage;
                Main.AM.playSound("hurt", null);
                timeLastHit = System.currentTimeMillis();
            }
        }

        if(playerHealth <= 0){
            Main.AM.stop("loop");
            Main.AM.stop("intro");
            glfwDestroyWindow(Main.window);
            failure();
        }

        if(slungusHealth<=0){
            slungusBeaten();
        }
    }

    private void failure(){
        Main.isBattling = false;
        Main.AM.playSound("glass", ()->{
            System.exit(0);
        });
    }

    private void slungusBeaten(){
        VideoLauncher.openYouTubeVideo("E9GgxIi-lGo");
        closeWindow();
        System.exit(0);
    }

    public class VideoLauncher {
        public static void openYouTubeVideo(String videoId) {
            String url = "https://www.youtube.com/watch?v=" + videoId;
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Desktop is not supported. Cannot open URL.");
            }
        }
    }


    public void spawnProjectile(double xdir, double ydir) {
        double theta = Math.atan2(ydir - py, xdir - px);
        projectiles.add(new Projectile(px, py, (float) Math.cos(theta)*4, (float) Math.sin(theta)*4, false));
    }

    public void spawnProjectileEnemy(float enemyX, float enemyY) {
        double theta = Math.atan2(enemyY - py - (down ? speed * delta : up ? speed * -delta : 0), enemyX - px - (left ? speed * delta : right ? speed * -delta : 0));
        projectiles.add(new Projectile(enemyX, enemyY, (float) -Math.cos(theta)*6, (float) -Math.sin(theta)*6, true));
    }

    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getPx() {
        return px;
    }

    public float getPy() {
        return py;
    }

    private boolean collidingWithSlungus(){
        return x-128 < px + 16 && x -128 + 256 > px && y-128 < py + 16 && y-128 + 256 > py;
    }
    

}
