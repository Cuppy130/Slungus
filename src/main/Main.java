package main;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector2i;

import engine.Model;
import game.Gamestate;
public class Main {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        Window window = new Window("Click slungus to win!", new Vector2i(WIDTH, HEIGHT));
        Gamestate.init();

        Model clickSlungus = new Model(new Vector2f(WIDTH, HEIGHT), Gamestate.clickToWin);
        
        window.setIcon("/res/images/Slungus.png");
        window.show();
        window.setVSync(true);



        glfwSetKeyCallback(window.getId(), (windowId, key, scancode, action, mods) -> {

            if(action == GLFW_PRESS) {
                Gamestate.keys[key] = true;
            } else if(action == GLFW_RELEASE) {
                Gamestate.keys[key] = false;
            }

            if(Gamestate.screen == 0) {
                if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(windowId, true);
                }
            }
            
            if(Gamestate.screen == 1) {
                if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    Gamestate.screen = 0;
                }
            }
        });

        glfwSetMouseButtonCallback(window.getId(), (windowId, button, action, mods) -> {
            if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                if(Gamestate.screen == 0) {
                    //get the mouse position
                    double[] xpos = new double[1];
                    double[] ypos = new double[1];
                    glfwGetCursorPos(windowId, xpos, ypos);
                    int x = (int)xpos[0];
                    int y = (int)ypos[0];
                    if(x > 169 && x < 274 && y > 20 && y < 78) {
                        Gamestate.screen = 1;
                    } if( y > 99){
                        Gamestate.screen = 2;
                    }
                    System.out.println("x: " + x + " y: " + y);
                }
            }
        });
        
        while (!window.shouldClose()) {
            Gamestate.audio.update();
            window.clear();
            if(Gamestate.screen == 0) {
                clickSlungus.render(new Vector2f(WIDTH/2, HEIGHT/2));
            }
            if(Gamestate.screen == 1) {
                window.drawBackground(Gamestate.slungusTexture);
                Gamestate.player.render();
                Gamestate.player.update(window.getDelta());
            }
            
            window.update();
        }
    }

}