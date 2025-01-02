package entity;

import org.joml.Vector2f;

import engine.Model;
import engine.Texture;
import misc.Color;

public class Entity {
    protected Vector2f size;
    protected Vector2f position;
    protected Model model;
    protected double direction;
    protected double speed;
    protected Vector2f target;
    protected boolean alive;
    protected Color color;
    protected int health;
    protected int damage;
    protected double maxSpeed = 1;

    // weapon
    protected int barrels = 1; // number of barrels, 4 creates a diamond pattern
    protected int spread = 0; // degrees
    protected int fireRate = 1; // 1 / sec

    // looking vector


    public Entity(Vector2f position, Vector2f size) {
        this.size = size;
        this.target = new Vector2f(0, 0);
        this.position = position;
        this.direction = 0;
        this.speed = 0;
        this.alive = true;
        this.color = new Color(1,1,1);
        this.health = 100;
        this.damage = 10;
        this.model = new Model(size, new Texture("/res/images/Slungus.png"));
    }

    public void update(double delta) {
        if (position.distance(target) < 0.1) {
            position = target;
        } else {
            position.add((float)(Math.cos(direction) * speed * delta), (float)(Math.sin(direction) * speed * delta));
        }
    }

    public void render() {
        model.render(position);
    }

    public void damage(int damage) {
        health -= damage;
        if (health <= 0) {
            alive = false;
        }
    }

    public void setTarget(Vector2f target) {
        this.target = target;
        direction = Math.atan2(target.y - position.y, target.x - position.x);
    }
}
