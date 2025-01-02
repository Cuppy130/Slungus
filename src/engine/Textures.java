package engine;

import java.util.ArrayList;
import java.util.List;

public class Textures {
    public static List<Texture> textures = new ArrayList<>();

    public static void add(Texture texture) {
        textures.add(texture);
    }

    public static void cleanup() {
        for (Texture texture : textures) {
            texture.cleanup();
        }
    }

    public static Texture get(String path) {
        for (Texture texture : textures) {
            if (texture.getPath().equals(path)) {
                return texture;
            }
        }
        return null;
    }
}
