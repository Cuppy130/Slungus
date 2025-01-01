package entity;

import org.joml.Vector2f;

import engine.Model;
import main.Main;

public class Entity {
    protected Vector2f position;
    protected Vector2f velocity;
    protected float speed;
    protected int hp;
    protected int maxHp;
    protected int size;
    protected int bulletDamage;
    protected Model entityModel;
    public float direction;

    public Entity(float x, float y, int size){
        position = new Vector2f(x, y);
        velocity = new Vector2f();
        this.size = size;
        entityModel = new Model(new float[]{0, 0, size, 0, 0, size, size, size}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, Main.slungusTexture);
    }

    public void takeDamage(int hp){
        this.hp-=hp;
    }

    public void update(float delta){
        // old code
        // x += vx * delta * 10;
        // y += vy * delta * 10;

        // new code
        position.add(velocity.mul(delta * 10));
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public void draw(){
        entityModel.render(position.sub(new Vector2f(size/2)));
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
