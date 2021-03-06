package link.duane.mon4j;

import link.duane.mon4j.alerts.ArmAlertListener;
import link.duane.mon4j.services.IService;
import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class Engine
{
    private final Logger log = LoggerFactory.getLogger( Engine.class );

    private Scheduler scheduler;

    public boolean start()
    {
        try
        {
            // Grab the Scheduler instance from the Factory
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            List<IService> services = Services.getInstance().getServices();

            for ( IService service : services )
            {
                try
                {
                    scheduler.scheduleJob( service.getJob(), service.getTrigger() );
                }
                catch ( SchedulerException se )
                {
                    log.error( se.getMessage() );
                }
            }

            // services.stream().forEach((service) -> {
            // try
            // {
            // scheduler.scheduleJob(service.getJob(), service.getTrigger());
            // }
            // catch( SchedulerException se )
            // {
            // log.error(se.getMessage());
            // }
            // });

            // Listen for Job Executions throwing alerts
            ArmAlertListener listen = new ArmAlertListener();

            scheduler.getListenerManager().addJobListener( listen, EverythingMatcher.allJobs() );
            // start it off
            scheduler.start();

        }
        catch ( SchedulerException se )
        {
            log.error( se.getMessage() );
            return false;
        }
        log.info( "Engine started" );
        return true;
    }

    public void stop()
    {
        try
        {
            if ( scheduler != null )
            {
                scheduler.shutdown( true );
            }
        }
        catch ( SchedulerException ex )
        {
            log.error( ex.getMessage() );
        }

        log.info( "Engine shutdown" );
    }
}
