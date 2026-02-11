package examplemod.examples.mobs;

import examplemod.examples.settlement.jobs.ExampleLevelJob;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.friendly.human.humanShop.HumanShop;
import necesse.inventory.InventoryItem;

import java.util.Collections;
import java.util.List;

public class ExampleSettlerMob extends HumanShop {

    public ExampleSettlerMob() {
        // MUST pass 3 args (same pattern as FarmerHumanMob)
        super(500, 200, "examplesettler");

        // Unlock the job type for THIS settler only
        this.jobTypeHandler.getPriority("weeding").disabledBySettler = false;

        // Give them a tool to clear grass (optional, but nice)
        this.equipmentInventory.setItem(6, new necesse.inventory.InventoryItem("farmingscythe"));

        // Register handler so they can actually perform the job
        this.jobTypeHandler.setJobHandler(
                ExampleLevelJob.class,
                0, 0,      // cooldown min/max
                0, 4000,   // work-break buffer usage min/max (matches Forestry)
                (handler, worker) ->
                        !isOnWorkBreak()
                                && !isOnStrike()
                                && !hasCompletedMission()
                                && (!isSettler() || isSettlerWithinSettlement())
                                && !isInventoryFull(true),
                foundJob -> ExampleLevelJob.getJobSequence(this, isSettler(), foundJob)
        );
    }

    @Override
    public List<InventoryItem> getRecruitItems(ServerClient client) {
        // Optional: if trapped, don’t allow recruiting
        if (isTrapped()) return Collections.emptyList();

        // Simple recruit cost (you can make this random like vanilla does)
        return Collections.singletonList(new InventoryItem("exampleitem", 10));
    }
}