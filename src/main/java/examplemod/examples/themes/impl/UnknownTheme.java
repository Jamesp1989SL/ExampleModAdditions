package examplemod.examples.themes.impl;

import java.util.Random;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;

import examplemod.examples.themes.BiomeTheme;

public class UnknownTheme extends ThemeBase implements BiomeTheme {

    @Override
    public String getBiomeStringID() {
        return "unknown";
    }

    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public String getDisabledMessage() {
        return "Unknown is a fallback biome for when biome data is missing. usually when a mod is removed. Theme conversion is disabled for it.";
    }
    @Override
    public void paintTile(Level level, int tileX, int tileY, Random random) {
    }

    @Override
    public TicketSystemList<GameObject> createObjectTickets(Level level) {
TicketSystemList<GameObject> t = new TicketSystemList<>();
    t.addObject(1, ObjectRegistry.getObject("air"));
    return t;

    }

}
