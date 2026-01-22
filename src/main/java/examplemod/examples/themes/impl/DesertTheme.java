    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class DesertTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "desert";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.sandID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2500, air);
        t.addObject(180, ObjectRegistry.getObject("cactus"));
        t.addObject(90, ObjectRegistry.getObject("palmtree"));
        t.addObject(110, ObjectRegistry.getObject("sandsurfacerock"));
        t.addObject(160, ObjectRegistry.getObject("sandsurfacerocksmall"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    String id = obj.getStringID();
    // sand rocks look good clustered
    return !(id.equals("sandsurfacerock") || id.equals("sandsurfacerocksmall"));
}

    }
