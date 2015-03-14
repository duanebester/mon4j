/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package link.duane.mon4j.services;

import link.duane.mon4j.jobs.CpuJob;
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
public class CpuService
    implements IService
{
    private static final String JOB = "cpuJob";

    private static final String TRIGGER = "cpuTrigger";

    private static final String GROUP = "cpuGroup";

    Properties props = Props.getInstance().getProperties();

    private final JobDetail job;

    private final Trigger trigger;

    public CpuService()
    {
        job = newJob( CpuJob.class ).withIdentity( JOB, GROUP ).build();

        job.getJobDataMap().put( ARM_VALUE, Double.parseDouble( props.getProperty( "cpu.armValue" ) ) );
        job.getJobDataMap().put( ARM_DELAY, Integer.parseInt( props.getProperty( "cpu.armDelay" ) ) );
        job.getJobDataMap().put( RE_ARM_VALUE, Double.parseDouble( props.getProperty( "cpu.reArmValue" ) ) );
        job.getJobDataMap().put( OPERATOR, props.getProperty( "cpu.operator" ) );

        // Compute a time that is on the next round minute
        // Date runTime = evenSecondDate(new Date());

        // Trigger the job to run on the next round minute
        trigger =
            newTrigger().withIdentity( TRIGGER, GROUP ).startNow().withSchedule( simpleSchedule().withIntervalInSeconds( Integer.parseInt( props.getProperty( "cpu.intervalInSeconds" ) ) ).repeatForever() ).build();
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
