package examplemod.examples.objectentity;

import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObject;

import examplemod.examples.settlement.jobs.ExampleLevelJob;

public class ExampleJobObjectEntity extends ObjectEntity {

    // Config
    private final int radiusTiles = 25;

    // State
    private long lastScanTime;
    private int scanDX;
    private int scanDY;

    public ExampleJobObjectEntity(Level level, int tileX, int tileY) {
        // NOTE: ObjectEntity constructor needs (level, type, x, y)
        super(level, "examplejobobjectentity", tileX, tileY);
        this.shouldSave = true;

        this.scanDX = -radiusTiles;
        this.scanDY = -radiusTiles;
        this.lastScanTime = 0L;
    }

    @Override
    public void init() {
        super.init();
        if (this.lastScanTime <= 0L) {
            this.lastScanTime = getWorldEntity().getWorldTime();
        }
    }

    @Override
    public void serverTick() {
        super.serverTick();

        long now = getWorldEntity().getWorldTime();
        // run once per second (worldTime is ms)
        long scanIntervalMs = 1000;
        if (now < this.lastScanTime + scanIntervalMs) {
            return;
        }
        this.lastScanTime = now;

        Level level = getLevel();

        // how many tiles we check each scan burst
        int tilesPerRun = 120;
        for (int i = 0; i < tilesPerRun; i++) {
            int x = this.tileX + scanDX;
            int y = this.tileY + scanDY;

            // advance scan cursor (square spiral-ish over the area)
            scanDX++;
            if (scanDX > radiusTiles) {
                scanDX = -radiusTiles;
                scanDY++;
                if (scanDY > radiusTiles) {
                    scanDY = -radiusTiles;
                }
            }

            if (!level.isTileWithinBounds(x, y)) continue;

            // Don’t clear decorative / player-placed grass
            if (level.objectLayer.isPlayerPlaced(x, y)) continue;

            LevelObject lo = level.getLevelObject(x, y);
            if (lo.object == null || !lo.object.isGrass) continue;

            // Add your example job
            level.jobsLayer.addJob(new ExampleLevelJob(x,y));
        }
    }
}
