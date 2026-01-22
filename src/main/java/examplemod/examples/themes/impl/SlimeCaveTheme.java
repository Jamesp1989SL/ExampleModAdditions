    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class SlimeCaveTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "slimecave";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.getTileID("slimerocktile"));
        if (random.nextFloat() < 0.06f) {
            setTile(level, tileX, tileY, TileRegistry.getTileID("liquidslimetile"));
        }

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1850, air);
        t.addObject(520, ObjectRegistry.getObject("slimerock"));
        t.addObject(260, ObjectRegistry.getObject("slimecaverock"));
        t.addObject(320, ObjectRegistry.getObject("slimecaverocksmall"));
        t.addObject(200, ObjectRegistry.getObject("groundslime"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false;
}

    }
