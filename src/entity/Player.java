package entity;

import java.util.concurrent.ScheduledFuture;

import org.lwjgl.glfw.GLFW;

import engine.TimerUtils;
import game.Game;
import main.Main;

public class Player extends Entity {

    public boolean mouseDown = false;

    // For unlocking the second phase
    private boolean hit = false;

    // Shop stuff
    private int points = 0;

    // Firing
    private int fireRate = 8; // x second/bullets
    private ScheduledFuture<?> timer;
    
    // Sheild
    private int sheildHealth = 25;
    private int sheildHealthMax = 25;

    // Buffs
    private int[] buffs = new int[3];


    //movement
    public boolean moveRight = false;
    public boolean moveLeft = false;
    public boolean moveUp = false;
    public boolean moveDown = false;

    private boolean isSetUp = false;

    public Player(){
        super(320, 480/4*3, 32);
        bulletDamage = 2;
        speed = 1;
        hp = 100;
        maxHp = 10000;
        buffs[0] = 1;
    }

    @Override
    public void takeDamage(int hp) {
        hit = true;
        if(sheildHealth<=0){
            super.takeDamage(hp);
        } else {
            sheildHealth -= 1;
        }
    }

    public void setup(Game game) {
        if (isSetUp) {
            TimerUtils.stopInterval(timer);
        } else {
            isSetUp = true;
        }
        timer = TimerUtils.interval(()->{
            if(mouseDown){
                double[] xpos = new double[1];
                double[] ypos = new double[1];
                GLFW.glfwGetCursorPos(Main.window, xpos, ypos);
                game.spawnBullet(position, bulletDamage, buffs[0], (float)Math.atan2(ypos[0] - getY(), xpos[0] - getX()));
                Main.AM.playSound("lazer", null);
            }
        }, 1000/fireRate);
    }
    
    public void gainPoints(int p){
        points += p;
    }

    public boolean isHit() {
        return hit;
    }

    public int[] getBuffs() {
        return buffs;
    }
    public int getPoints() {
        return points;
    }

    public void setSheildHealth(int sheildHealth) {
        this.sheildHealth = sheildHealth;
    }
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }
    public int getSheildHealth() {
        return sheildHealth;
    }

    @Override
    public void update(float delta) {
        // if(moveLeft){
        //     x -= speed * delta * 10;
        // }
        // if(moveRight){
        //     x += speed * delta * 10;
        // }
        // if(moveUp){
        //     y -= speed * delta * 10;
        // }
        // if(moveDown){
        //     y += speed * delta * 10;
        // }

        if(moveLeft){
            position.x -= speed * delta * 10;
        }
        if(moveRight){
            position.x += speed * delta * 10;
        }
        if(moveUp){
            position.y -= speed * delta * 10;
        }
        if(moveDown){
            position.y += speed * delta * 10;
        }
    }
}
