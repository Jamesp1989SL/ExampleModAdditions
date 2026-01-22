package examplemod.examples.themes.impl;

import java.util.Random;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.engine.registries.TileRegistry;

import examplemod.examples.themes.BiomeTheme;

public class AscendedVoidTheme extends ThemeBase implements BiomeTheme {

    @Override
    public String getBiomeStringID() {
        return "ascendedvoid";
    }

    @Override
    public void paintTile(Level level, int tileX, int tileY, Random random) {
// Ascended Void is preset-based; don't paint tiles by default.

    }

    @Override
    public TicketSystemList<GameObject> createObjectTickets(Level level) {
TicketSystemList<GameObject> t = new TicketSystemList<>();
    t.addObject(1, ObjectRegistry.getObject("air"));
    return t;

    }

}
