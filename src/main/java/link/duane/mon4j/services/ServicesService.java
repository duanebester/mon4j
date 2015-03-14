package link.duane.mon4j.services;

import link.duane.mon4j.jobs.ServiceJob;
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

public class ServicesService
    implements IService
{
    private static final String JOB = "servicesJob";

    private static final String TRIGGER = "servicesTrigger";

    private static final String GROUP = "servicesGroup";

    Properties props = Props.getInstance().getProperties();

    private final JobDetail job;

    private final Trigger trigger;

    public ServicesService()
    {

        job = newJob( ServiceJob.class ).withIdentity( JOB, GROUP ).build();

        job.getJobDataMap().put( SERVICE_DOWN_TIME, props.getProperty( "services.timeDownSeconds" ) );

        // Compute a time that is on the next round minute
        // Date runTime = evenSecondDate(new Date());

        // Trigger the job to run on the next round minute
        trigger =
            newTrigger().withIdentity( TRIGGER, GROUP ).startNow().withSchedule( simpleSchedule().withIntervalInSeconds( Integer.parseInt( props.getProperty( "services.intervalInSeconds" ) ) ).repeatForever() ).build();
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
