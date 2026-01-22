    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class SwampTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "swamp";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.swampGrassID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2200, air);
        t.addObject(130, ObjectRegistry.getObject("willowtree"));
        t.addObject(140, ObjectRegistry.getObject("swampgrass"));
        t.addObject(90, ObjectRegistry.getObject("wildmushroom"));
        t.addObject(70, ObjectRegistry.getObject("purpleflowerpatch"));
        t.addObject(90, ObjectRegistry.getObject("swampsurfacerock"));
        t.addObject(140, ObjectRegistry.getObject("swampsurfacerocksmall"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    String id = obj.getStringID();
    return !(id.equals("swampsurfacerock") || id.equals("swampsurfacerocksmall"));
}

    }
