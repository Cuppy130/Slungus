package game;
import org.lwjgl.opengl.GL11;

public class Shop {
    public boolean open = false;
    private static final float[] color = new float[]{0f, 0f, 0f, 0.5f};

    public Shop() {
    }

    public void open() {
        open = !open;
    }

    public void render() {
        if (!open) {
            return; // Don't render if the shop is closed
        }

        // Enable blending for transparency
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Set the color with transparency
        GL11.glColor4f(color[0], color[1], color[2], color[3]);

        // Render the shop rectangle
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);   // Bottom-left
        GL11.glVertex2f(800, 0); // Bottom-right
        GL11.glVertex2f(800, 600); // Top-right
        GL11.glVertex2f(0, 600);   // Top-left
        GL11.glEnd();

        // Disable blending to return to normal rendering
        GL11.glDisable(GL11.GL_BLEND);
    }
}
