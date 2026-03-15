package app.morphe.extension.spotify.misc;

import java.util.Map;
import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public class UnlockPremiumPatch {
    private static String currentUrl;
    private static String currentUri;

    public static void overrideAttributes(Map<String, Object> attributes) {
        // Force premium attributes
        attributes.put("type", "premium");
        attributes.put("product", "premium");
        attributes.put("streaming-rules", "");
        attributes.put("financial-product", "pr:premium,it:premium");
        attributes.put("license-code", "premium");
        
        // Enable specific features
        attributes.put("ad-config", "");
        attributes.put("ads", "0");
        attributes.put("catalogue", "premium");
        attributes.put("high-bitrate", "1");
        attributes.put("shuffling", "0");
        attributes.put("skipping", "0");
        
        Logger.printDebug(() -> "Spotify attributes overridden to Premium");
    }

    public static void onContextMenu(String url, String uri) {
        currentUrl = url;
        currentUri = uri;
        Logger.printDebug(() -> "Context menu captured: " + url + " (" + uri + ")");
    }
}
