package examplemod.examples.themes.impl;

import java.util.Random;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.engine.registries.TileRegistry;

import examplemod.examples.themes.BiomeTheme;

public class PlainsTheme extends ThemeBase implements BiomeTheme {

    @Override
    public String getBiomeStringID() {
        return "plains";
    }

    @Override
    public void paintTile(Level level, int tileX, int tileY, Random random) {
setTile(level, tileX, tileY, TileRegistry.plainsGrassID);

    }

    @Override
    public TicketSystemList<GameObject> createObjectTickets(Level level) {
GameObject air = ObjectRegistry.getObject("air");
    TicketSystemList<GameObject> t = new TicketSystemList<>();
    t.addObject(2300, air);
    t.addObject(140, ObjectRegistry.getObject("mapletree"));
    t.addObject(140, ObjectRegistry.getObject("birchtree"));
    t.addObject(90, ObjectRegistry.getObject("surfacerock"));
    t.addObject(140, ObjectRegistry.getObject("surfacerocksmall"));
    t.addObject(120, ObjectRegistry.getObject("leafpile"));
    t.addObject(90, ObjectRegistry.getObject("whiteflowerpatch"));
    t.addObject(80, ObjectRegistry.getObject("wildsunflower"));
    return t;

    }

}
