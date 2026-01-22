    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class TempleTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "temple";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    // Temple levels are mostly preset-generated in vanilla.
// We approximate the look: mostly sand brick with some wood floor mixed in.
        if (random.nextFloat() < 0.15f) {
            setTile(level, tileX, tileY, TileRegistry.woodFloorID);
        } else {
            setTile(level, tileX, tileY, TileRegistry.sandBrickID);
        }

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2600, air);
        t.addObject(120, ObjectRegistry.getObject("deepsandstonecolumn"));
        t.addObject(120, ObjectRegistry.getObject("deepsandstonewall"));
        t.addObject(60, ObjectRegistry.getObject("vase"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    String id = obj.getStringID();
    return !(id.equals("deepsandstonewall") || id.equals("deepsandstonecolumn"));
}

    }
