package entity;

import engine.Model;
import main.Main;

public class Entity {
    protected float x;
    protected float y;
    protected float vx;
    protected float vy;
    protected float speed;
    protected int hp;
    protected int maxHp;
    protected int size;
    protected int bulletDamage;
    protected Model entityModel;
    public float direction;

    public Entity(float x, float y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
        entityModel = new Model(new float[]{0, 0, size, 0, 0, size, size, size}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, Main.slungusTexture);
    }

    public void takeDamage(int hp){
        this.hp-=hp;
    }

    public void update(float delta){
        x += vx * delta * 10;
        y += vy * delta * 10;
    }

    public void draw(){
        entityModel.render(x-size/2, y-size/2);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
