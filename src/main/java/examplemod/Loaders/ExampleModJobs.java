package examplemod.Loaders;

import examplemod.examples.settlement.jobs.ExampleLevelJob;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.registries.JobTypeRegistry;
import necesse.engine.registries.LevelJobRegistry;
import necesse.entity.mobs.job.JobType;

public class ExampleModJobs {

    public static void load(){
        // 1) Register the job type
        JobTypeRegistry.registerType("weeding",
                new JobType(
                        true,  // canChangePriority (shows in settlement UI)
                        true,  // defaultDisabledBySettler (locked for normal settlers)
                        new LocalMessage("jobs", "weedingname"),
                        new LocalMessage("jobs", "weedingtip")
                )
        );

        // 2) Register our ExampleLevelJob //DEBUG
        LevelJobRegistry.registerJob("weedgrass", ExampleLevelJob .class, "weeding");
    }

}
