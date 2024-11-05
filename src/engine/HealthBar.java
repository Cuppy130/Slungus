package engine;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.lwjgl.opengl.GL11;

import main.Main;

public class HealthBar {
    private final int width;
    private final int height;

    public HealthBar(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw(float x, float y, float percentage) {
        percentage = Math.max(0, Math.min(percentage, 1));
        float filledWidth = width * percentage;

        
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();

        glBindTexture(GL_TEXTURE_2D, Main.red);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + filledWidth, y);
        GL11.glVertex2f(x + filledWidth, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();

        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }
}