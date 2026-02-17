package examplemod.examples.tiles;

import java.awt.Color;
import java.awt.Point;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.gfx.gameTexture.GameTextureSection;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.gameObject.GameObject;
import necesse.level.gameTile.TerrainSplatterTile;
import necesse.level.maps.Level;
import necesse.level.maps.regionSystem.SimulatePriorityList;

/**
 * ExampleGrassTile
 * This is a ground tile.
 * It does 3 main things:
 *  1) Drops a seed sometimes when mined.
 *  2) Can grow a grass object on top of it ("examplegrass").
 *  3) Can spread onto nearby dirt tiles.
 */
public class ExampleGrassTile extends TerrainSplatterTile {

    // How often the grass OBJECT should grow on this tile
    public static double growChance = GameMath.getAverageSuccessRuns(7000.0D);

    // How often this TILE should spread onto dirt next to it
    public static double spreadChance = GameMath.getAverageSuccessRuns(850.0D);

    // Used only for picking a random sprite row (visual variation)
    private final GameRandom drawRandom = new GameRandom();

    public ExampleGrassTile() {
        // Texture file: resources/tiles/examplegrasstile.png
        super(false, "examplegrasstile");

        this.mapColor = new Color(70, 120, 40); // minimap colour
        this.canBeMined = true;                 // player can mine/remove it
        this.isOrganic = true;                  // marks it as organic
    }

    @Override
    public LootTable getLootTable(Level level, int tileX, int tileY) {
        // 4% chance to drop a grass seed when mined
        return new LootTable(new ChanceLootItem(0.04F, "examplegrassseed"));
    }

    @Override
    public void addSimulateLogic(Level level, int x, int y, long ticks,
                                 SimulatePriorityList list, boolean sendChanges) {
        // Off-screen simulation: schedule growth while the chunk is not actively ticking
        addSimulateGrow(level, x, y, growChance, ticks, "examplegrass", list, sendChanges);
    }

    /**
     * Off-screen growth: schedule placing the grass object after enough simulated time passes.
     */
    public static void addSimulateGrow(Level level, int tileX, int tileY, double chance, long ticks,
                                       String growObjectID, SimulatePriorityList list, boolean sendChanges) {

        // Only grow if there is no object on this tile
        if (level.getObjectID(tileX, tileY) != 0) return;

        // Convert the chance into a rough amount of time before it should succeed
        double runs = Math.max(1.0D, GameMath.getRunsForSuccess(chance, GameRandom.globalRandom.nextDouble()));
        long remainingTicks = (long) (ticks - runs);
        if (remainingTicks <= 0L) return;

        GameObject obj = ObjectRegistry.getObject(ObjectRegistry.getObjectID(growObjectID));

        // canPlace == null means it's allowed to place here
        if (obj.canPlace(level, tileX, tileY, 0, false) != null) return;

        // Add a delayed task to place the object later
        list.add(tileX, tileY, remainingTicks, () -> {
            if (obj.canPlace(level, tileX, tileY, 0, false) == null) {
                obj.placeObject(level, tileX, tileY, 0, false);
                level.objectLayer.setIsPlayerPlaced(tileX, tileY, false); // natural growth
                if (sendChanges) level.sendObjectUpdatePacket(tileX, tileY);
            }
        });
    }

    @Override
    public double spreadToDirtChance() {
        // Controls how fast dirt turns into this grass tile when nearby
        return spreadChance;
    }

    @Override
    public void tick(Level level, int x, int y) {
        // Only the server should change the world
        if (!level.isServer()) return;

        // Grow the grass OBJECT on empty tiles sometimes
        if (level.getObjectID(x, y) == 0 && GameRandom.globalRandom.getChance(growChance)) {
            GameObject grassObj = ObjectRegistry.getObject(ObjectRegistry.getObjectID("examplegrass"));
            if (grassObj.canPlace(level, x, y, 0, false) == null) {
                grassObj.placeObject(level, x, y, 0, false);
                level.objectLayer.setIsPlayerPlaced(x, y, false);
                level.sendObjectUpdatePacket(x, y);
            }
        }
    }

    @Override
    public Point getTerrainSprite(GameTextureSection terrainTexture, Level level, int tileX, int tileY) {
        // Pick a random row for the sprite, but keep it consistent per tile position
        int row;
        synchronized (drawRandom) {
            row = drawRandom.seeded(getTileSeed(tileX, tileY))
                    .nextInt(terrainTexture.getHeight() / 32);
        }
        return new Point(0, row); // column 0, chosen row
    }

    @Override
    public int getTerrainPriority() {
        // Used when tiles overlap/compete in drawing/spreading rules
        return 100;
    }
}