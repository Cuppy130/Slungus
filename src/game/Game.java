package game;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;

import engine.Collision;
import engine.HealthBar;
import engine.RenderTaskQueue;
import engine.WinScreen;

import java.util.ArrayList;
import java.util.List;

import entity.*;
import main.Main;

public class Game {
    private boolean finalPhase = false;
    private Player player;
    private SlungusBoss slungus;
    private List<Slungus> slungusList = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private HealthBar healthBar;
    // private Shop shop;

    public Game(){
        player = new Player();
        slungus = new SlungusBoss(this);
        healthBar = new HealthBar(100, 20);
        // shop = new Shop();
        setup();
    }

    public void setup() {
        
        glfwSetMouseButtonCallback(Main.window, (windowHandle, button, action, mods) -> {
            if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_1){
                player.mouseDown = true;
            } else if(action == GLFW_RELEASE && button == GLFW_MOUSE_BUTTON_1){
                player.mouseDown = false;
            }
        });
        glfwSetKeyCallback(Main.window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    if(key==GLFW_KEY_A){
                        player.moveLeft = true;
                    }
                    if(key==GLFW_KEY_D){
                        player.moveRight = true;
                    }
                    if(key==GLFW_KEY_W){
                        player.moveUp = true;
                    }
                    if(key==GLFW_KEY_S){
                        player.moveDown = true;
                    }

                    if(key==GLFW_KEY_TAB){
                        // shop.open();
                        // Main.AM.setBackgroundEffect("loop", shop.open);
                        // Main.AM.setVolume("loop", 0.2f);
                    }
                }

                if (action == GLFW_RELEASE) {
                    if(key==GLFW_KEY_A){
                        player.moveLeft = false;
                    }
                    if(key==GLFW_KEY_D){
                        player.moveRight = false;
                    }
                    if(key==GLFW_KEY_W){
                        player.moveUp = false;
                    }
                    if(key==GLFW_KEY_S){
                        player.moveDown = false;
                    }
                }
            }
        });
        player.setup(this);
    }

    public void update(float delta){
        // if(shop.open)return;
        player.update(delta);
        slungus.update(delta);
        for (Slungus slungus : slungusList) {
            slungus.update(delta);
        }
        float ssize = slungus.getSize();
        float psize = player.getSize();
        if(Collision.AABB(slungus.getX()-ssize/2, slungus.getY()-ssize/2, ssize, player.getX()-psize/2, player.getY()-psize/2, psize)){
            player.takeDamage(5);
            Main.AM.stop("hurt");
            Main.AM.playSound("hurt", null);
        }
        for (Bullet bullet : bullets) {
            bullet.update(delta);
            float bsize = bullet.getSize();
            if(Collision.AABB(bullet.getX()-bsize/2, bullet.getY()-bsize/2, bsize, player.getX()-psize/2, player.getY()-psize/2, psize)){
                if(bullet.isEnemyBullet()){
                    player.takeDamage(bullet.getDamage());
                    RenderTaskQueue.addTask(()->{
                        bullets.remove(bullet);
                    });
                    Main.AM.stop("hurt");
                    Main.AM.playSound("hurt", null);
                }
            }

            // for (Slungus slungus : slungusList) {
            //     if(Collision.AABB(slungus.getX()-ssize/2, slungus.getY()-ssize/2, ssize, bullet.getX()-bsize/2, bullet.getY()-bsize/2, bsize)){

            //     }
            // }

            if(!Collision.AABB(0, 0, 640, bullet.getX(), bullet.getY(), bsize)){
                RenderTaskQueue.addTask(()->{
                    bullets.remove(bullet);
                });
            }
            
            if(Collision.AABB(bullet.getX()-bsize/2, bullet.getY()-bsize/2, bsize, slungus.getX()-ssize/2, slungus.getY()-ssize/2, ssize)){
                if(!bullet.isEnemyBullet()){
                    slungus.takeDamage(bullet.getDamage());
                    RenderTaskQueue.addTask(()->{
                        bullets.remove(bullet);
                    });
                    Main.AM.stop("hurt2");
                    Main.AM.playSound("hurt2", null);
                }
            }
        }
        if(player.getX()<16){
            player.setX(16);
        } else if(player.getX()>640-16){
            player.setX(640-16);
        }
        if(player.getY()<16){
            player.setY(16);
        } else if(player.getY()>480-16){
            player.setY(480-16);
        }
        
        if(player.getHp()<=0){
            Main.isBattling = false;
            glfwDestroyWindow(Main.window);
            Main.AM.stop("intro");
            Main.AM.stop("loop");
            Main.AM.playSound("glass", ()->{
                System.exit(0);
            });
        } else if(slungus.getHp()<=0){
            if(player.isHit()){
                
            } else {
                // Main.AM.stop("intro");
                // Main.AM.stop("loop");
                // finalPhase = true;
                // slungus.setX(640/2);
                // slungus.setY(psize);
                // slungus.stop();
                // Main.AM.playSound("slungus", ()->{
                //     System.out.println("final phase");
                // });
            }
            slungus.setX(320);
            slungus.setY(-240);
            Main.isBattling = false;
            Main.AM.stop("intro");
            Main.AM.stop("loop");
            Main.AM.playSound("congratulations", ()->{
                System.exit(0);
            });
        }
    }

    public void draw(){
        GL11.glColor4f(1f, 1f, 1f, 1f);
        player.draw();
        slungus.draw();
        for (Slungus slungus2 : slungusList) {
            slungus2.draw();
        }
        for (Bullet bullet : bullets) {
            bullet.draw();
        }

        float slungusHealth = (float) slungus.getHp() / (float) slungus.getMaxHp();
        healthBar.draw(640-100-10, 480-20-10, slungusHealth);

        // shop.render();
    }

    public void spawnEnemyBullet(float x, float y, int damage){
        // if(shop.open)return;
        RenderTaskQueue.addTask(()->{
            bullets.add(new Bullet(x, y, true, damage, (float) Math.atan2(player.getY() - slungus.getY(), player.getX() - slungus.getX())));
        });
    }

    public void spawnBullet(float x, float y, int damage, int multiplier, float direction){
        // if(shop.open)return;
        RenderTaskQueue.addTask(()->{
            bullets.add(new Bullet(x, y, false, damage*multiplier, direction));
        });
    }

    public Player getPlayer() {
        return player;
    }
}