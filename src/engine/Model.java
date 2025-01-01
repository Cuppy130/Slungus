package engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class Model {
    private int drawCount;
    private int v_id; 
    private int t_id;
    public int t;
    // public float x = 0;
    // public float y = 0;
    public Vector2f position = new Vector2f(0, 0);

    public Model(float[] vertices, float[] tex_coords, int tex_id) {
        drawCount = vertices.length / 2;
        t = tex_id;

        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);
        
        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {
        render(position);
    }

    public void render(Vector2f position) {
        glPushMatrix();
        glTranslatef(position.x, position.y, 0);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexPointer(2, GL_FLOAT, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);
        glBindTexture(GL_TEXTURE_2D, t);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, drawCount);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glPopMatrix();
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public void cleanup() {
        glDeleteBuffers(t_id);
        glDeleteBuffers(v_id);
    }
}
