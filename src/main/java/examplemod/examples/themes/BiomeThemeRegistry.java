package examplemod.examples.themes;

import java.util.HashMap;
import java.util.Map;

public final class BiomeThemeRegistry {
    private static final Map<String, BiomeTheme> THEMES = new HashMap<>();

    private BiomeThemeRegistry() {
    }

    public static void register(BiomeTheme theme) {
        THEMES.put(theme.getBiomeStringID(), theme);
    }

    public static BiomeTheme get(String biomeStringID) {
        return THEMES.get(biomeStringID);
    }
}
