package com.securelink.mon4j.services;

import com.securelink.mon4j.jobs.EventLogJob;
import com.securelink.mon4j.util.Props;
import java.util.Properties;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class EventLogService
    implements IService
{
    private static final String JOB = "eventLogJob";

    private static final String TRIGGER = "eventLogTrigger";

    private static final String GROUP = "eventLogGroup";

    Properties props = Props.getInstance().getProperties();

    private final JobDetail job;

    private final Trigger trigger;

    public EventLogService()
    {
        job = newJob( EventLogJob.class ).withIdentity( JOB, GROUP ).build();

        // Trigger the job to run on the next round minute
        trigger =
            newTrigger().withIdentity( TRIGGER, GROUP ).startNow().withSchedule( simpleSchedule().withIntervalInSeconds( Integer.parseInt( props.getProperty( "eventLog.intervalInSeconds" ) ) ).repeatForever() ).build();
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
