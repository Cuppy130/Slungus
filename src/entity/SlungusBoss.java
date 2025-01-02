package entity;

import org.joml.Vector2f;

public class SlungusBoss extends Entity {
    public SlungusBoss(Vector2f position) {
        super(position, new Vector2f(512));
        this.health = 25;
        this.speed = 0.1;
    }

    public void update(double delta) {
        super.update(delta);
    }

    public void render() {
        super.render();
    }
    
}
