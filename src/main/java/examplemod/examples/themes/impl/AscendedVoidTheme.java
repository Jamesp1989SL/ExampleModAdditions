package examplemod.examples.themes.impl;

import java.util.Random;

import examplemod.examples.themes.BiomeTheme;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;

public class AscendedVoidTheme extends ThemeBase implements BiomeTheme {

    @Override
    public String getBiomeStringID() {
        return "ascendedvoid";
    }

    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public String getDisabledMessage() {
        return "Ascended Void is a preset/special biome (no normal tiles/objects/spawns). Theme conversion is disabled for it.";
    }

    @Override
    public void paintTile(Level level, int tileX, int tileY, Random random) {
        // no-op
    }

    @Override
    public TicketSystemList<GameObject> createObjectTickets(Level level) {
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1, ObjectRegistry.getObject("air"));
        return t;
    }
}
