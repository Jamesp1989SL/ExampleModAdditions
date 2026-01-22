    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class GraveyardTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "graveyard";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.cryptAshID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2000, air);
        t.addObject(100, ObjectRegistry.getObject("cryptcoffin"));
        t.addObject(100, ObjectRegistry.getObject("cryptcolumn"));
        t.addObject(100, ObjectRegistry.getObject("cryptgravestone1"));
        t.addObject(100, ObjectRegistry.getObject("cryptgravestone2"));
        t.addObject(100, ObjectRegistry.getObject("deadwoodtree"));
        t.addObject(100, ObjectRegistry.getObject("vase"));
        t.addObject(30, ObjectRegistry.getObject("deadwoodcandles"));
        t.addObject(5, ObjectRegistry.getObject("deadwoodbench"));
        t.addObject(5, ObjectRegistry.getObject("deadwoodchair"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    // Graveyard looks OK with some clustering
    return true;
}

@Override
public void decorate(Level level, int tileX, int tileY, Random random) {
    if (random.nextFloat() > 0.20f) return;
    GameObject cryptgrass = ObjectRegistry.getObject("cryptgrass");
    if (level.getObjectID(tileX, tileY) == 0 && cryptgrass.canPlace(level, tileX, tileY, 0, false) == null) {
        cryptgrass.placeObject(level, tileX, tileY, 0, false);
    }
}

    }
