package link.duane.mon4j.services;

import link.duane.mon4j.jobs.DiskJob;
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

public class DiskService
    implements IService
{
    private static final String JOB = "diskJob";

    private static final String TRIGGER = "diskTrigger";

    private static final String GROUP = "diskGroup";

    private final JobDetail job;

    private final Trigger trigger;

    Properties props = Props.getInstance().getProperties();

    public DiskService()
    {
        job = newJob( DiskJob.class ).withIdentity( JOB, GROUP ).build();

        job.getJobDataMap().put( ARM_VALUE, Double.parseDouble( props.getProperty( "disk.armValue" ) ) );
        job.getJobDataMap().put( ARM_DELAY, Integer.parseInt( props.getProperty( "disk.armDelay" ) ) );
        job.getJobDataMap().put( RE_ARM_VALUE, Double.parseDouble( props.getProperty( "disk.reArmValue" ) ) );
        job.getJobDataMap().put( OPERATOR, props.getProperty( "disk.operator" ) );

        // Compute a time that is on the next round minute
        // Date runTime = evenSecondDate(new Date());

        // Trigger the job to run on the next round minute
        trigger =
            newTrigger().withIdentity( TRIGGER, GROUP ).startNow().withSchedule( simpleSchedule().withIntervalInSeconds( Integer.parseInt( props.getProperty( "disk.intervalInSeconds" ) ) ).repeatForever() ).build();
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
