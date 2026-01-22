    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class SettlementRuinsTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "settlementruins";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    // Incursion mixes farmland and crypt path
        if (random.nextFloat() < 0.25f) {
            setTile(level, tileX, tileY, TileRegistry.getTileID("cryptpath"));
        } else {
            setTile(level, tileX, tileY, TileRegistry.getTileID("farmland"));
        }

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2100, air);
        t.addObject(120, ObjectRegistry.getObject("deadwoodtree"));
        t.addObject(140, ObjectRegistry.getObject("basaltcaverock"));
        t.addObject(200, ObjectRegistry.getObject("basaltcaverocksmall"));
        t.addObject(80, ObjectRegistry.getObject("cryptwall"));
        t.addObject(60, ObjectRegistry.getObject("ascendedwall"));
        t.addObject(70, ObjectRegistry.getObject("cryptgravestone1"));
        t.addObject(70, ObjectRegistry.getObject("cryptgravestone2"));
        t.addObject(60, ObjectRegistry.getObject("deadwoodcandles"));
        t.addObject(25, ObjectRegistry.getObject("deadwoodcandelabra"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false;
}

@Override
public void decorate(Level level, int tileX, int tileY, Random random) {
    if (random.nextFloat() > 0.18f) return;
    GameObject cryptgrass = ObjectRegistry.getObject("cryptgrass");
    if (level.getObjectID(tileX, tileY) == 0 && cryptgrass.canPlace(level, tileX, tileY, 0, false) == null) {
        cryptgrass.placeObject(level, tileX, tileY, 0, false);
    }
}

    }
