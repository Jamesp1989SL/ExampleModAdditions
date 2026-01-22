    package examplemod.examples.themes.impl;

    import java.util.Random;

    import necesse.engine.registries.ObjectRegistry;
    import necesse.engine.util.TicketSystemList;
    import necesse.level.gameObject.GameObject;
    import necesse.level.maps.Level;
    import necesse.engine.registries.TileRegistry;

    import examplemod.examples.themes.BiomeTheme;

    public class SnowTheme extends ThemeBase implements BiomeTheme {

        @Override
        public String getBiomeStringID() {
            return "snow";
        }

        @Override
        public void paintTile(Level level, int tileX, int tileY, Random random) {
    setTile(level, tileX, tileY, TileRegistry.snowID);

        }

        @Override
        public TicketSystemList<GameObject> createObjectTickets(Level level) {
    GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> t = new TicketSystemList<>();
        t.addObject(2400, air);
        t.addObject(190, ObjectRegistry.getObject("pinetree"));
        t.addObject(90, ObjectRegistry.getObject("snowsurfacerock"));
        t.addObject(140, ObjectRegistry.getObject("snowsurfacerocksmall"));
        t.addObject(90, ObjectRegistry.getObject("blueflowerpatch"));
        t.addObject(80, ObjectRegistry.getObject("wildiceblossom"));
        t.addObject(120, ObjectRegistry.getObject("snowpile0"));
        t.addObject(120, ObjectRegistry.getObject("snowpile1"));
        t.addObject(120, ObjectRegistry.getObject("snowpile2"));
        t.addObject(120, ObjectRegistry.getObject("snowpile3"));
        return t;

        }
    @Override
public boolean requireAdjacentClear(GameObject obj) {
    String id = obj.getStringID();
    return !(id.equals("snowsurfacerock") || id.equals("snowsurfacerocksmall"));
}

    }
