package themer.themes.impl;

import java.util.Random;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.engine.registries.TileRegistry;

import themer.themes.BiomeTheme;

public class ForestTheme extends ThemeBase implements BiomeTheme {

    @Override
    public String getBiomeStringID() {
        return "forest";
    }

    @Override
    public void paintTile(Level level, int tileX, int tileY, Random random) {
setTile(level, tileX, tileY, TileRegistry.grassID);

    }

    @Override
    public TicketSystemList<GameObject> createObjectTickets(Level level) {
GameObject air = ObjectRegistry.getObject("air");
    TicketSystemList<GameObject> t = new TicketSystemList<>();
    t.addObject(2200, air);
    t.addObject(120, ObjectRegistry.getObject("oaktree"));
    t.addObject(120, ObjectRegistry.getObject("sprucetree"));
    t.addObject(90, ObjectRegistry.getObject("surfacerock"));
    t.addObject(140, ObjectRegistry.getObject("surfacerocksmall"));
    t.addObject(90, ObjectRegistry.getObject("redflowerpatch"));
    t.addObject(90, ObjectRegistry.getObject("yellowflowerpatch"));
    t.addObject(60, ObjectRegistry.getObject("wildfiremone"));
    t.addObject(80, ObjectRegistry.getObject("grass"));
    return t;

    }

}
