package examplemod.examples.patches;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import necesse.entity.mobs.job.EntityJobWorker;
import necesse.entity.mobs.job.FoundJob;
import necesse.entity.mobs.job.JobTypeHandler;

public final class JobFinderSafePatch {
    private JobFinderSafePatch() {}

    @SuppressWarnings({"rawtypes"})
    public static Stream<FoundJob> safeStreamFoundJobs(JobTypeHandler handler, EntityJobWorker worker) {
        if (handler == null || worker == null) return Stream.empty();

        Builder<FoundJob> b = Stream.builder();

        Collection subs = handler.getJobHandlers();

        for (Object o : subs) {
            if (!(o instanceof JobTypeHandler.SubHandler)) continue;

            JobTypeHandler.SubHandler sub = (JobTypeHandler.SubHandler) o;

            Iterator it = sub.streamFoundJobsFiltered(worker).iterator();
            while (it.hasNext()) {
                b.add((FoundJob) it.next());
            }
        }

        return b.build();
    }
}
