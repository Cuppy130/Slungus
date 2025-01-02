package main;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;

import engine.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.createCapabilities;


import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class Window {
    // delta time variables
    private long lastTime;
    private long timer;
    private long now;
    
    // window properties
    private String title;
    private Vector2i size;
    private Vector2i position;
    private boolean fullscreen;
    private long id;
    public Window(String title, Vector2i size) {
        this.title = title;
        this.size = size;
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        id = glfwCreateWindow(size.x, size.y, "Click slungus to win!", 0, 0);
        if (id == 0) {
            glfwTerminate();
            throw new RuntimeException("Failed to create GLFW window");
        }
        glfwMakeContextCurrent(id);
        createCapabilities();
        glfwSwapInterval(1);

        // Center the window to the primary monitor
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(id, (videoMode.width() - size.x) / 2, (videoMode.height() - size.y) / 2);

        // Enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Set up the viewport
        glViewport(0, 0, size.x, size.y);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, size.x, size.y, 0, -1, 1);
        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(id, title);
    }

    public void clear(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void clear(float r, float g, float b) {
        glClearColor(r, g, b, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void setSize(Vector2i size) {
        this.size = size;
        glfwSetWindowSize(id, size.x, size.y);
    }

    public void setSize(int width, int height) {
        setSize(new Vector2i(width, height));
    }

    public void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            setSize(videoMode.width(), videoMode.height());
            glfwSetWindowMonitor(id, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());
        } else {
            setSize(size.x, size.y);
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowMonitor(id, 0, (videoMode.width() - size.x) / 2, (videoMode.height() - size.y) / 2, size.x, size.y, videoMode.refreshRate());
        }
    }

    public void setVSync(boolean vsync) {
        glfwSwapInterval(vsync ? GL_TRUE : GL_FALSE);
    }

    public void setIcon(String path) {
        try(InputStream in = Window.class.getResourceAsStream(path)){
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + path);
            }
            int width = image.getWidth();
            int height = image.getHeight();

            ByteBuffer icon = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    icon.put((byte) ((pixel >> 16) & 0xFF));
                    icon.put((byte) ((pixel >> 8) & 0xFF));
                    icon.put((byte) (pixel & 0xFF));
                    icon.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            icon.flip();
            
            GLFWImage.Buffer icons = GLFWImage.malloc(1);
            GLFWImage glfwImage = GLFWImage.create();
            glfwImage.set(width, height, icon);
            icons.put(0, glfwImage);
            glfwSetWindowIcon(id, icons);
            icons.free();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load icon");
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void update() {
        //delta
        now = System.nanoTime();
        double delta = (now - lastTime) / 1000000000.0;
        lastTime = now;
        timer += delta;
        if (timer >= 1) {
            timer = 0;
        }

        //frame limiter
        while (now - lastTime < 1000000000 / 60) {
            now = System.nanoTime();
        }
        lastTime = now;

        glfwSwapBuffers(id);
        glfwPollEvents();
    }

    public void show() {
        glfwShowWindow(id);
    }

    public void cleanup() {
        glfwDestroyWindow(id);
        glfwTerminate();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(id);
    }

    public void drawBackground(Texture tex) {
        tex.bind();
        glColor3f(0.5f, 0.5f, 0.5f);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(0, 0);
        glTexCoord2f(1, 0); glVertex2f(size.x, 0);
        glTexCoord2f(1, 1); glVertex2f(size.x, size.y);
        glTexCoord2f(0, 1); glVertex2f(0, size.y);
        glEnd();
        glColor3f(1, 1, 1);
        tex.unbind();
    }
    
    public void setPosition(Vector2i position) {
        this.position = position;
        glfwSetWindowPos(id, position.x, position.y);
    }

    public void setPosition(int x, int y) {
        setPosition(new Vector2i(x, y));
    }

    public Vector2i getPosition() {
        return position;
    }

    public Vector2i getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void toggleFullscreen() {
        setFullscreen(!fullscreen);
    }

    public void setWindowed() {
        setFullscreen(false);
    }

    public void enableResizing(boolean resizable) {
        glfwSetWindowAttrib(id, GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
    }

    public double getDelta() {
        return (now - lastTime) / 1000000000.0;
    }
}
