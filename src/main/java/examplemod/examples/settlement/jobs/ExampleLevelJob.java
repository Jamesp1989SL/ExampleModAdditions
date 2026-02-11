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

public class ExampleLevelJob extends MineObjectLevelJob {

    public ExampleLevelJob(int tileX, int tileY) {
        super(tileX, tileY);
    }

    public ExampleLevelJob(LoadData save) {
        super(save);
    }

    @Override
    public boolean isValid() {
        // Basic job validity + level presence, etc.
        return super.isValid();
    }

    @Override
    public boolean isValidObject(LevelObject object) {
        // Don't clear decorative/player-placed grass
        if (getLevel().objectLayer.isPlayerPlaced(this.tileX, this.tileY)) return false;

        // Only target grass objects
        return object.object != null && object.object.isGrass;
    }

    @Override
    public boolean isSameJob(LevelJob other) {
        // Helps jobsLayer dedupe so your post doesn't spam the same job repeatedly
        return other instanceof ExampleLevelJob
                && other.tileX == this.tileX
                && other.tileY == this.tileY;
    }

    @Override
    public boolean shouldSave() {
        // Post will recreate jobs as needed
        return false;
    }

    public static <T extends ExampleLevelJob> JobSequence getJobSequence(
            EntityJobWorker worker, final boolean useItem, final FoundJob<T> foundJob
    ) {
        LevelObject target = foundJob.job.getObject();

        // target/object can be null if the job got invalidated between pickup and execution
        LocalMessage msg = new LocalMessage(
                "activities",
                "examplejob",
                "target",
                target != null && target.object != null ? target.object.getLocalization() : new LocalMessage("ui", "unknown")
        );

        final GameLinkedListJobSequence seq = new GameLinkedListJobSequence(msg);

        seq.add(new MineObjectActiveJob(
                worker,
                foundJob.priority,
                foundJob.job.tileX,
                foundJob.job.tileY,
                lo -> (!foundJob.job.isRemoved() && foundJob.job.isValidObject(lo)),
                foundJob.job.reservable,
                "farmingscythe",
                5,
                250,
                0
        ) {
            @Override
            public void onObjectDestroyed(ObjectDamageResult result) {
                addItemPickupJobs(foundJob.priority, result, seq);
                foundJob.job.remove();
            }
        });

        return seq;
    }
}
