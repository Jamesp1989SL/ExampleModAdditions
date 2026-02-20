package examplemod.examples.patches;

import java.util.stream.Stream;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.job.EntityJobWorker;
import necesse.entity.mobs.job.FoundJob;
import necesse.entity.mobs.job.JobFinder;
import necesse.entity.mobs.job.JobTypeHandler;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = JobFinder.class, name = "streamFoundJobs", arguments = {})
public class JobFinderStreamFoundJobsHandlersPatch {

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onEnter(
            @Advice.FieldValue("handler") JobTypeHandler handler,
            @Advice.FieldValue("worker") EntityJobWorker worker,
            @Advice.Local("out") Stream<FoundJob> out
    ) {
        out = JobFinderSafePatch.safeStreamFoundJobs(handler, worker);
        return true; // skip vanilla method body
    }

    @Advice.OnMethodExit
    static void onExit(@Advice.Local("out") Stream<FoundJob> out,
                       @Advice.Return(readOnly = false) Stream<FoundJob> ret) {
        ret = out;
    }
}
