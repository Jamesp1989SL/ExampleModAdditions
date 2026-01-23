    package themer.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import themer.themes.BiomeTheme;

    public class SpiderCastleTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "spidercastle";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.getTileID("spidercobbletile"));

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1900, air);
        t.addObject(700, ObjectRegistry.getObject("spiderrock"));
        t.addObject(120, ObjectRegistry.getObject("spideregg"));
        t.addObject(120, ObjectRegistry.getObject("crate"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false; // webby clutter
}

    }
