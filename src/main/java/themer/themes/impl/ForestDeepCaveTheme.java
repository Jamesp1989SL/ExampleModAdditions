    package themer.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import themer.themes.BiomeTheme;

    public class ForestDeepCaveTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "forestdeepcave";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.deepRockID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1700, air);
        t.addObject(520, ObjectRegistry.getObject("deepcaverock"));
        t.addObject(680, ObjectRegistry.getObject("deepcaverocksmall"));
        t.addObject(90, ObjectRegistry.getObject("copperoredeeprock"));
        t.addObject(70, ObjectRegistry.getObject("ironoredeeprock"));
        t.addObject(60, ObjectRegistry.getObject("goldoredeeprock"));
        t.addObject(40, ObjectRegistry.getObject("tungstenoredeeprock"));
        t.addObject(25, ObjectRegistry.getObject("lifequartzdeeprock"));
        t.addObject(25, ObjectRegistry.getObject("upgradesharddeeprock"));
        t.addObject(20, ObjectRegistry.getObject("alchemysharddeeprock"));
        t.addObject(18, ObjectRegistry.getObject("shadowessencedeeprock"));
        t.addObject(35, ObjectRegistry.getObject("obsidianrock"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false; // caves look better clustered
}

    }
