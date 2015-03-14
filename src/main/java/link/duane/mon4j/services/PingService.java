package link.duane.mon4j.services;

import link.duane.mon4j.jobs.PingJob;
import link.duane.mon4j.util.Props;
import java.util.Properties;
import static org.quartz.JobBuilder.newJob;

import org.quartz.JobDetail;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class PingService
    implements IService
{
    private static final String JOB = "pingJob";

    private static final String TRIGGER = "pingTrigger";

    private static final String GROUP = "pingGroup";

    Properties props = Props.getInstance().getProperties();

    private final JobDetail job;

    private final Trigger trigger;

    public PingService()
    {
        job = newJob( PingJob.class ).withIdentity( JOB, GROUP ).build();

        job.getJobDataMap().put( IP_ADDRESSES, props.getProperty( "ping.ips" ) );
        job.getJobDataMap().put( FAIL_COUNT, props.getProperty( "ping.failCount" ) );

        // Compute a time that is on the next round minute
        // Date runTime = evenSecondDate(new Date());

        // Trigger the job to run on the next round minute
        trigger =
            newTrigger().withIdentity( TRIGGER, GROUP ).startNow().withSchedule( simpleSchedule().withIntervalInSeconds( Integer.parseInt( props.getProperty( "ping.intervalInSeconds" ) ) ).repeatForever() ).build();
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
