    package themer.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import themer.themes.BiomeTheme;

    public class DesertDeepCaveTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "desertdeepcave";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.deepSandstoneID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1700, air);
        t.addObject(520, ObjectRegistry.getObject("deepsandcaverock"));
        t.addObject(680, ObjectRegistry.getObject("deepsandcaverocksmall"));
        t.addObject(90, ObjectRegistry.getObject("copperoredeepsandstonerock"));
        t.addObject(70, ObjectRegistry.getObject("ironoredeepsandstonerock"));
        t.addObject(60, ObjectRegistry.getObject("goldoredeepsandstonerock"));
        t.addObject(25, ObjectRegistry.getObject("lifequartzdeepsandstonerock"));
        t.addObject(25, ObjectRegistry.getObject("upgradesharddeepsandstonerock"));
        t.addObject(20, ObjectRegistry.getObject("alchemysharddeepsandstonerock"));
        t.addObject(18, ObjectRegistry.getObject("primordialessencedeepsandstonerock"));
        t.addObject(60, ObjectRegistry.getObject("deepsandstonecolumn"));
        t.addObject(60, ObjectRegistry.getObject("deepsandstonewall"));
        t.addObject(40, ObjectRegistry.getObject("vase"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false;
}

    }
