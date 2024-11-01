package engine;

import static org.lwjgl.opengl.GL11.*;

public class BossBar {
    private float maxHealth;
    private float currentHealth;
    private float x, y; // Position of the bar
    private float width, height; // Size of the bar

    public BossBar(float maxHealth, float x, float y, float width, float height) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updateHealth(float newHealth) {
        currentHealth = Math.max(0, Math.min(newHealth, maxHealth));
    }

    public void render() {
        float healthPercentage = currentHealth / maxHealth;
        
        glColor4f(1f, 0f, 0f, 1);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();

        glColor4f(1f, 1f, 0f, 1);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width * healthPercentage, y);
        glVertex2f(x + width * healthPercentage, y + height);
        glVertex2f(x, y + height);
        glEnd();
    }

    public void cleanup() {
        // Reset OpenGL color to white in case of leftover state
        glColor4f(1f, 1f, 1f, 1f);
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }
}
