package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Vector2f;

import game.Gamestate;

public class Player extends Entity {
    private int sheildHealth = 25;
    private int sheildMaxHealth = 25;
    private int sheildRegen = 1;
    private int sheildRegenRate = 1;

    public Player(Vector2f position) {
        super(position, new Vector2f(64));
        this.health = 100;
        this.speed = 0.25;
    }

    public void update(double delta) {
        if(Gamestate.screen == 2){
            if (sheildHealth < sheildMaxHealth) {
                sheildHealth += sheildRegen;
                if (sheildHealth > sheildMaxHealth) {
                    sheildHealth = sheildMaxHealth;
                }
            }
            
            position.x = clamp(position.x, size.x/2, 800 - size.x/2);
            position.y = clamp(position.y, size.y/2, 600 - size.y/2);

            if(Gamestate.keyPressed(GLFW_KEY_W)) {
                position.y -= speed * delta;
            }
            System.out.println("Player position: " + position);
        }
        super.update(delta);
    }

    public float clamp(float val, float min, float max) {
        return val < min ? min : val > max ? max : val;
    }

    public void render() {
        super.render();
    }

    @Override
    public void damage(int damage) {
        if (sheildHealth > 0) {
            sheildHealth -= damage;
            if (sheildHealth < 0) {
                health += sheildHealth;
                sheildHealth = 0;
            }
        } else {
            health -= damage;
        }
        if (health <= 0) {
            alive = false;
        }
    }

    public Vector2f getPosition() {
        return position;
    }
}
