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
    public Texture t;
    public Vector2f position = new Vector2f(0, 0);

    public Model (Vector2f size, Texture texture){
        this(size, new Vector2f(1), texture);
    }

    public Model(Vector2f size, Vector2f tex, Texture texture){
        this.t = texture;
        float[] vertices = new float[]{
            -size.x / 2, -size.y / 2,
            size.x / 2, -size.y / 2,
            -size.x / 2, size.y / 2,
            size.x / 2, size.y / 2
        };
        float[] tex_coords = new float[]{
            0, 0,
            tex.x, 0,
            0, tex.y,
            tex.x, tex.y
        };
        drawCount = vertices.length / 2;

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
        t.bind();
        glDrawArrays(GL_TRIANGLE_STRIP, 0, drawCount);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glPopMatrix();
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanup() {
        glDeleteBuffers(t_id);
        glDeleteBuffers(v_id);
    }
}
