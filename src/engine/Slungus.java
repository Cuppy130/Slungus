package engine;

public class Slungus {
    private Model model;
    public Slungus(){
        model = new Model(new float[]{0, 0, 640, 0, 0, 480, 640, 480}, new float[]{0, 0, 1, 0, 0, 1, 1, 1}, TextureUtils.loadTexture("/res/slugnus.png"));
    }

    public void draw(){
        model.render();
    }

    public void cleanup() {
        model.cleanup();
    }

}
