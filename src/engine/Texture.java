package engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Texture {
    private String path = "";
    private int id;
    private Vector2i size;

    public Texture(String path) {

        this.path = path;

        if(Textures.get(path) != null) {
            Texture t = Textures.get(path);
            this.id = t.getId();
            this.size = t.getSize();
            this.path = path;
            return;
        }

        if(path == null) {
            throw new RuntimeException("Texture not found: " + path);
        }
        try(InputStream in = Texture.class.getResourceAsStream(path)) {
            BufferedImage image = ImageIO.read(in);
            if(in == null) {
                throw new RuntimeException("Texture not found: " + path);
            }
            if(image == null) {
                throw new RuntimeException("Failed to load image: " + path);
            }
            size = new Vector2i(image.getWidth(), image.getHeight());
            int[] pixels = new int[size.x * size.y];
            image.getRGB(0, 0, size.x, size.y, pixels, 0, size.x);
            ByteBuffer data = BufferUtils.createByteBuffer(size.x * size.y * 4);
            for(int y = 0; y < size.y; y++) {
                for(int x = 0; x < size.x; x++) {
                    int pixel = pixels[y * size.x + x];
                    data.put((byte) ((pixel >> 16) & 0xFF));
                    data.put((byte) ((pixel >> 8) & 0xFF));
                    data.put((byte) (pixel & 0xFF));
                    data.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            data.flip();
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size.x, size.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            glBindTexture(GL_TEXTURE_2D, 0);
            Textures.add(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Vector2i getSize() {
        return size;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    public int getId() {
        return id;
    }
}
