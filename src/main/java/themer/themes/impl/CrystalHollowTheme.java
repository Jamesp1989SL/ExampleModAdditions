    package themer.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import themer.themes.BiomeTheme;

    public class CrystalHollowTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "crystalhollow";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.getTileID("crystaltile"));

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2100, air);
        t.addObject(120, ObjectRegistry.getObject("amethystclustersmall"));
        t.addObject(80, ObjectRegistry.getObject("amethystclusterpure"));
        t.addObject(120, ObjectRegistry.getObject("rubyclustersmall"));
        t.addObject(80, ObjectRegistry.getObject("rubyclusterpure"));
        t.addObject(120, ObjectRegistry.getObject("sapphireclustersmall"));
        t.addObject(80, ObjectRegistry.getObject("sapphireclusterpure"));
        t.addObject(120, ObjectRegistry.getObject("topazclustersmall"));
        t.addObject(80, ObjectRegistry.getObject("topazclusterpure"));
        t.addObject(120, ObjectRegistry.getObject("emeraldclustersmall"));
        t.addObject(80, ObjectRegistry.getObject("emeraldclusterpure"));
        t.addObject(70, ObjectRegistry.getObject("pearlescentshard"));
        t.addObject(80, ObjectRegistry.getObject("crate"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false;
}

    }
