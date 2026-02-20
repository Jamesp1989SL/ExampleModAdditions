package examplemod.examples.maps.biomes;

import examplemod.Loaders.ExampleModObjects;
import examplemod.Loaders.ExampleModTiles;
import examplemod.examples.ExampleLootTable;
import necesse.engine.AbstractMusicList;
import necesse.engine.MusicList;
import necesse.engine.registries.MusicRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.world.biomeGenerator.BiomeGeneratorStack;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.lootTable.LootItemInterface;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.LootItemList;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.Biome;
import necesse.level.maps.biomes.MobSpawnTable;
import necesse.level.maps.presets.RandomCaveChestRoom;
import necesse.level.maps.presets.caveRooms.CaveRuins;
import necesse.level.maps.presets.set.ChestRoomSet;
import necesse.level.maps.presets.set.ColumnSet;
import necesse.level.maps.presets.set.WallSet;
import necesse.level.maps.regionSystem.Region;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controls:
 *  - surface ground tile
 *  - base cave/deep cave tiles + rock objects
 *  - region decoration passes (surface / cave / deep cave)
 *  - spawns, music, and structure hooks
 */
public class ExampleBiome extends Biome {

    // =========================================================================
    // Spawns
    // =========================================================================

    public static final MobSpawnTable surfaceCritters = new MobSpawnTable()
            .include(Biome.defaultSurfaceCritters);

    public static final MobSpawnTable caveCritters = new MobSpawnTable()
            .include(Biome.defaultCaveCritters);

    public static final MobSpawnTable surfaceMobs = new MobSpawnTable()
            .add(30, "examplemob");

    public static final MobSpawnTable caveMobs = new MobSpawnTable()
            .add(100, "examplemob");

    public static final MobSpawnTable deepCaveMobs = new MobSpawnTable()
            .add(100, "examplemob");

    public ExampleBiome() {
        super();
        this.setGenerationWeight(1.0F);
    }

    // =========================================================================
    // Base tiles / rocks
    // =========================================================================

    /**
     * Surface ground tile.
     * If it can't be found, we fall back to grass so worlds still load.
     */
    @Override
    public int getGenerationTerrainTileID() {
        int exampleGrass = TileRegistry.getTileID("examplegrasstile");
        return (exampleGrass == -1) ? TileRegistry.grassID : exampleGrass;
    }

    /**
     * Cave floor tile used in this biome.
     */
    @Override
    public int getGenerationCaveTileID() {
        return ExampleModTiles.EXAMPLE_TILE_ID;
    }

    /**
     * Cave rock object used in this biome.
     */
    @Override
    public int getGenerationCaveRockObjectID() {
        return ExampleModObjects.EXAMPLE_BASE_ROCK_ID;
    }

    /**
     * Deep cave floor tile used in this biome.
     */
    @Override
    public int getGenerationDeepCaveTileID() {
        return ExampleModTiles.EXAMPLE_TILE_ID;
    }

    /**
     * Deep cave rock object used in this biome.
     * If you ever add a separate deep version, swap it in here.
     */
    @Override
    public int getGenerationDeepCaveRockObjectID() {
        return ExampleModObjects.EXAMPLE_BASE_ROCK_ID;
    }

    // Set up the loot interface for our boss summon extra drop
    public static LootItemInterface randomExampleBossSummonDrop = new LootItemList(new ChanceLootItem(1.00F, "examplebosssummonitem"));

    // =========================================================================
    // Generator setup (veins)
    // =========================================================================

    @Override
    public void initializeGeneratorStack(BiomeGeneratorStack stack) {
        super.initializeGeneratorStack(stack);

        // Trees on the surface
        stack.addRandomSimplexVeinsBranch("exampleTrees", 2.0F, 0.2F, 1.0F, 0);

        // Ore veins underground
        stack.addRandomVeinsBranch("exampleCaveOre", 0.60F, 3, 6, 0.4F, 2, false);
        stack.addRandomVeinsBranch("exampleDeepCaveOre", 0.60F, 3, 6, 0.4F, 2, false);
    }

    // =========================================================================
    // Region passes
    // =========================================================================

    @Override
    public void generateRegionSurfaceTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionSurfaceTerrain(region, stack, random);

        final int grassTile = getGenerationTerrainTileID();

        stack.startPlaceOnVein(this, region, random, "exampleTrees")
                .onlyOnTile(grassTile)
                .chance(0.10D)
                .placeObject("exampletree");

        stack.startPlace(this, region, random)
                .chance(0.40D)
                .onlyOnTile(grassTile)
                .placeObject("examplegrass");
    }

    @Override
    public void generateRegionCaveTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionCaveTerrain(region, stack, random);

        // Ore veins: place on our cave rock object
        stack.startPlaceOnVein(this, region, random, "exampleCaveOre")
                .onlyOnObject(ExampleModObjects.EXAMPLE_BASE_ROCK_ID)
                .placeObjectForced("exampleorerock");

        // If you want crates / small rocks etc, add them here.
        // region.updateLiquidManager(); // only needed if you place/edit liquids
    }

    @Override
    public void generateRegionDeepCaveTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionDeepCaveTerrain(region, stack, random);

        // Ore veins: place on our deep cave rock object (same as base for now)
        stack.startPlaceOnVein(this, region, random, "exampleDeepCaveOre")
                .onlyOnObject(ExampleModObjects.EXAMPLE_BASE_ROCK_ID)
                .placeObjectForced("exampleorerock");

        // region.updateLiquidManager(); // only needed if you place/edit liquids
    }

    // =========================================================================
    // Debug + music
    // =========================================================================

    @Override
    public Color getDebugBiomeColor() {
        return new Color(128, 0, 128);
    }

    @Override
    public AbstractMusicList getLevelMusic(Level level, PlayerMob perspective) {
        return new MusicList(MusicRegistry.ForestPath);
    }
    // =========================================================================
    // Boss Summon Drop
    // =========================================================================

    // Add drop to journal
    @Override
    public LootTable getExtraBiomeMobDrops(LevelIdentifier levelIdentifier) {
        if (levelIdentifier == null)
            return new LootTable();
        if (levelIdentifier.equals(LevelIdentifier.CAVE_IDENTIFIER))
            return new LootTable(randomExampleBossSummonDrop);
        return new LootTable();
    }

    // Add Example Boss Summon Item
    @Override
    public LootTable getExtraMobDrops(Mob mob) {
        if (mob == null || mob.getLevel() == null) return super.getExtraMobDrops(mob);

        // Only in caves (or include deep caves too)
        LevelIdentifier id = mob.getLevel().getIdentifier();
        boolean isCave = LevelIdentifier.CAVE_IDENTIFIER.equals(id);
        // boolean isDeepCave = LevelIdentifier.DEEP_CAVE_IDENTIFIER.equals(id);

        if (isCave) {
            if (mob.isHostile && !mob.isSummoned) return new LootTable(randomExampleBossSummonDrop);
        }
        return super.getExtraMobDrops(mob);
    }

    // =========================================================================
    // Spawn selection
    // =========================================================================

    @Override
    public MobSpawnTable getCritterSpawnTable(Level level) {
        return level.isCave ? caveCritters : surfaceCritters;
    }

    @Override
    public MobSpawnTable getMobSpawnTable(Level level) {
        if (!level.isCave) return surfaceMobs;
        if (LevelIdentifier.DEEP_CAVE_IDENTIFIER.equals(level.getIdentifier())) return deepCaveMobs;
        return caveMobs;
    }

    // =========================================================================
    // Structures / presets
    // =========================================================================

    public RandomCaveChestRoom getNewCaveChestRoomPreset(GameRandom random, AtomicInteger lootRotation) {

        // WallSet("example") will look for:
        // examplewall, exampledoor, examplearrowtrap, exampleflametrap, etc.
        WallSet exampleWalls = new WallSet("example");

        // Keep columns the same as stone (optional)
        ColumnSet columns = ColumnSet.stone;

// This "set" is just a bundle of IDs that tells the chest-room preset
// which tiles/objects to use when it builds the room.
        ChestRoomSet exampleSet = new ChestRoomSet(
                "exampletile",                    // The floor tile name the preset should use
                "examplepressureplate",                // The pressure plate object to place in the room
                exampleWalls,                                // WallSet we made earlier. It supplies the wall + door + trap object by looking up IDs that start with "example"
                columns,                                     // Just the column style (visual decoration)
                "storagebox",                                // The chest object that will be placed in the room
                "examplewalltrap"        // A trap object ID to use (this must be a real registered object ID)
        );

// Now we build the actual room preset using that set.
        return getRandomCaveChestRoom(random, lootRotation, exampleSet);
    }

    private static RandomCaveChestRoom getRandomCaveChestRoom(
            GameRandom random,
            AtomicInteger lootRotation,
            ChestRoomSet exampleSet
    ) {
        // Create the chest room preset.
        // - ExampleLootTable.exampleloottable = what items go in the chest
        // - lootRotation = the counter used for "rotating" loot between different chest rooms
        // - exampleSet = which walls/door/plate/traps/chest to place
        RandomCaveChestRoom chestRoom = new RandomCaveChestRoom(
                random,
                ExampleLootTable.exampleloottable,
                lootRotation,
                exampleSet
        );

        // The base preset normally uses stone floor tiles.
        // This line swaps any stone floor the preset would place into our custom cave tile instead.
        chestRoom.replaceTile(TileRegistry.stoneFloorID, ExampleModTiles.EXAMPLE_TILE_ID);

        // Give the preset back to world gen. It will be placed later when the generator picks a location.
        return chestRoom;
    }


    public RandomCaveChestRoom getNewDeepCaveChestRoomPreset(GameRandom random, AtomicInteger unique) {
        return null;
    }


    public CaveRuins getNewCaveRuinsPreset(GameRandom random, AtomicInteger unique) {
        return null;
    }


    public CaveRuins getNewDeepCaveRuinsPreset(GameRandom random, AtomicInteger unique) {
        return null;
    }
}
