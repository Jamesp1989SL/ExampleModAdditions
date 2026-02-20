package examplemod.examples.settlement.jobs;

import necesse.engine.localization.message.LocalMessage;
import necesse.engine.save.LoadData;
import necesse.entity.ObjectDamageResult;
import necesse.entity.mobs.job.EntityJobWorker;
import necesse.entity.mobs.job.FoundJob;
import necesse.entity.mobs.job.GameLinkedListJobSequence;
import necesse.entity.mobs.job.JobSequence;
import necesse.entity.mobs.job.activeJob.MineObjectActiveJob;
import necesse.level.maps.LevelObject;
import necesse.level.maps.levelData.jobs.LevelJob;
import necesse.level.maps.levelData.jobs.MineObjectLevelJob;

/**
 * A simple settlement job:
 * "Go to this tile and clear the grass object there."
 * We extend MineObjectLevelJob because Necesse already has a job type for
 * destroying an object at a tile.
 */
public class ExampleLevelJob extends MineObjectLevelJob {

    // Create a new job at a tile position
    public ExampleLevelJob(int tileX, int tileY) {
        super(tileX, tileY);
    }

    // Create a job from saved data (not used if shouldSave() returns false)
    public ExampleLevelJob(LoadData save) {
        super(save);
    }

    @Override
    public boolean isValid() {
        // Use the base checks (it will call isValidObject on the current object)
        return super.isValid();
    }

    @Override
    public boolean isValidObject(LevelObject object) {
        // Do NOT let settlers clear objects that a player placed.
        if (getLevel().objectLayer.isPlayerPlaced(this.tileX, this.tileY)) return false;

        // Only allow this job to target grass objects.
        return object.object != null && object.object.isGrass;
    }

    @Override
    public boolean isSameJob(LevelJob other) {
        // Jobs system uses this to avoid duplicates.
        // If another ExampleLevelJob exists at the same tile, treat it as the same job.
        return other instanceof ExampleLevelJob
                && other.tileX == this.tileX
                && other.tileY == this.tileY;
    }

    @Override
    public boolean shouldSave() {
        // Don't save this job. The settlement can recreate it later if needed.
        return false;
    }

    /**
     * This builds the actual steps the settler will do.
     * Here we only add ONE step: mine/destroy the grass object.
     */
    public static <T extends ExampleLevelJob> JobSequence getJobSequence(
            EntityJobWorker worker, final boolean useItem, final FoundJob<T> foundJob
    ) {
        // Get the current object at the job tile (might be null if it changed)
        LevelObject target = foundJob.job.getObject();

        // Message shown for the job (in settlement UI)
        LocalMessage msg = new LocalMessage(
                "activities",
                "examplejob",
                "target",
                (target != null && target.object != null)
                        ? target.object.getLocalization()
                        : new LocalMessage("ui", "unknown")
        );

        // A list of work steps
        final GameLinkedListJobSequence seq = new GameLinkedListJobSequence(msg);

        // Add the work step: go to tile + hit the object until it breaks
        seq.add(new MineObjectActiveJob(
                worker,
                foundJob.priority,
                foundJob.job.tileX,
                foundJob.job.tileY,

                // Keep working only while the job still exists AND the object is still valid grass
                lo -> (!foundJob.job.isRemoved() && foundJob.job.isValidObject(lo)),

                // Reservation (stops 2 settlers trying to do the same tile)
                foundJob.job.reservable,

                // Item used for the "swing" animation (visual only)
                "farmingscythe",

                // Damage per hit to the object
                5,

                // Time per swing (ms)
                250,

                // Extra delay between swings (ms)
                0
        ) {
            @Override
            public void onObjectDestroyed(ObjectDamageResult result) {
                // Make pickup jobs for any drops
                addItemPickupJobs(foundJob.priority, result, seq);

                // Remove the job so it doesn't stay posted
                foundJob.job.remove();
            }
        });

        return seq;
    }
}