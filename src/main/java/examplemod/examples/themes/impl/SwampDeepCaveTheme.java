    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class SwampDeepCaveTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "swampdeepcave";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.deepSwampRockID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1650, air);
        t.addObject(520, ObjectRegistry.getObject("deepswampcaverock"));
        t.addObject(680, ObjectRegistry.getObject("deepswampcaverocksmall"));
        t.addObject(90, ObjectRegistry.getObject("copperoredeepswamprock"));
        t.addObject(70, ObjectRegistry.getObject("ironoredeepswamprock"));
        t.addObject(60, ObjectRegistry.getObject("goldoredeepswamprock"));
        t.addObject(40, ObjectRegistry.getObject("tungstenoredeepswamprock"));
        t.addObject(25, ObjectRegistry.getObject("lifequartzdeepswamprock"));
        t.addObject(30, ObjectRegistry.getObject("myceliumoredeepswamprock"));
        t.addObject(20, ObjectRegistry.getObject("upgradesharddeepswamprock"));
        t.addObject(15, ObjectRegistry.getObject("alchemysharddeepswamprock"));
        t.addObject(18, ObjectRegistry.getObject("bioessencedeepswamprock"));
        t.addObject(140, ObjectRegistry.getObject("deepswampgrass"));
        t.addObject(90, ObjectRegistry.getObject("deepswamptallgrass"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false;
}

    }
