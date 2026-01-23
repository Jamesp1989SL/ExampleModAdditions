    package themer.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import themer.themes.BiomeTheme;

    public class DungeonTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "dungeon";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.dungeonFloorID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2800, air);
        t.addObject(80, ObjectRegistry.getObject("dungeonwall"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    // walls can touch
    return !obj.getStringID().equals("dungeonwall");
}

    }
