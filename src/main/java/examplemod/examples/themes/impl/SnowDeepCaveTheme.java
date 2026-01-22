    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class SnowDeepCaveTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "snowdeepcave";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.deepSnowRockID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(1700, air);
        t.addObject(520, ObjectRegistry.getObject("deepsnowcaverock"));
        t.addObject(680, ObjectRegistry.getObject("deepsnowcaverocksmall"));
        t.addObject(90, ObjectRegistry.getObject("copperoredeepsnowrock"));
        t.addObject(70, ObjectRegistry.getObject("ironoredeepsnowrock"));
        t.addObject(60, ObjectRegistry.getObject("goldoredeepsnowrock"));
        t.addObject(40, ObjectRegistry.getObject("tungstenoredeepsnowrock"));
        t.addObject(25, ObjectRegistry.getObject("lifequartzdeepsnowrock"));
        t.addObject(30, ObjectRegistry.getObject("glacialoredeepsnowrock"));
        t.addObject(20, ObjectRegistry.getObject("upgradesharddeepsnowrock"));
        t.addObject(15, ObjectRegistry.getObject("alchemysharddeepsnowrock"));
        t.addObject(18, ObjectRegistry.getObject("cryoessencedeepsnowrock"));
        t.addObject(10, ObjectRegistry.getObject("ancientfossiloredeepsnowrock"));
        t.addObject(20, ObjectRegistry.getObject("fallingicicletrigger"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    return false;
}

    }
