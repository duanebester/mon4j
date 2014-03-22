package com.securelink.mon4j.services;

import com.securelink.mon4j.jobs.MemoryJob;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class MemoryService
    implements IService
{
    private static final String JOB = "memoryJob";

    private static final String TRIGGER = "memoryTrigger";

    private static final String GROUP = "memoryGroup";

    private final JobDetail job;

    private final Trigger trigger;

    public MemoryService()
    {
        job = newJob( MemoryJob.class ).withIdentity( JOB, GROUP ).build();

        // Compute a time that is on the next round minute
        // Date runTime = evenSecondDate(new Date());

        // Trigger the job to run on the next round minute
        trigger = newTrigger().withIdentity( TRIGGER, GROUP ).startNow().withSchedule( simpleSchedule().withIntervalInSeconds( 10 ).repeatForever() ).build();
    }

    /**
     * @return the job
     */
    @Override
    public JobDetail getJob()
    {
        return job;
    }

    /**
     * @return the trigger
     */
    @Override
    public Trigger getTrigger()
    {
        return trigger;
    }
}
