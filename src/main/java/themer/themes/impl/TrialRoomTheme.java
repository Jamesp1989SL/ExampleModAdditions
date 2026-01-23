package themer.themes.impl;

import java.util.Random;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.engine.registries.TileRegistry;

import themer.themes.BiomeTheme;

public class TrialRoomTheme extends ThemeBase implements BiomeTheme {

    @Override
    public String getBiomeStringID() {
        return "trialroom";
    }

    @Override
    public void paintTile(Level level, int tileX, int tileY, Random random) {
setTile(level, tileX, tileY, TileRegistry.rockID);

    }

    @Override
    public TicketSystemList<GameObject> createObjectTickets(Level level) {
TicketSystemList<GameObject> t = new TicketSystemList<>();
    t.addObject(1, ObjectRegistry.getObject("air"));
    return t;

    }

}
