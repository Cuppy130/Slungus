package engine;

import java.net.URL;

public class AssetLoader {

    public static URL loadAssetURL(String assetPath) {
        URL url = AssetLoader.class.getResource(assetPath);

        if (url == null) {
            throw new RuntimeException("Asset not found: " + assetPath);
        }

        return url;
    }
}