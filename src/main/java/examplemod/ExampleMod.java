package examplemod;

import examplemod.Loaders.*;
import examplemod.examples.maps.biomes.ExampleBiome;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.sound.SoundSettings;
import necesse.engine.sound.gameSound.GameSound;
import necesse.level.maps.biomes.Biome;

@ModEntry
public class ExampleMod {
    // Global access point for mod settings
    public static ExampleModSettings settings;

    // We define our static registered objects here, so they can be referenced elsewhere
    public static ExampleBiome EXAMPLE_BIOME;
    public static GameSound EXAMPLESOUND;
    public static SoundSettings EXAMPLESOUNDSETTINGS;

    // Load settings for the example mod from the external file defined in ExampleModSettings
    public ExampleModSettings initSettings() {
        settings = new ExampleModSettings();
        return settings;
    }

    public void init() {
        System.out.println("Hello world from my example mod!");

        // The examples are split into different classes here for readability, but you can register them directly here in init if you wish
        ExampleModCategories.load();
        ExampleModEvents.load();
        ExampleModJobs.load();
        ExampleModBiomes.load();
        ExampleModIncursions.load();
        ExampleModTiles.load();
        ExampleModObjects.load();
        ExampleModItems.load();
        ExampleModMobs.load();
        ExampleModSettlers.load();
        ExampleModProjectiles.load();
        ExampleModBuffs.load();
        ExampleModPackets.load();
    }

    public void initResources() {
        ExampleModResources.load();
    }

    public void postInit() {
        // load our recipes from the ExampleRecipes class so we can keep this class easy to read
        ExampleModRecipes.registerRecipes();


        // Add our example mob to default cave mobs.
        // Spawn tables use a ticket/weight system. In general, common mobs have about 100 tickets.
        Biome.defaultCaveMobs
                .add(100, "examplemob");
    }

}
