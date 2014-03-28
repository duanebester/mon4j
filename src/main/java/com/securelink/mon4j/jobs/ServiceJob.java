package com.securelink.mon4j.jobs;

import java.util.List;
import org.hyperic.sigar.win32.Service;
import org.hyperic.sigar.win32.Win32Exception;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class ServiceJob
    extends BaseJob
{
    public Logger log = LoggerFactory.getLogger( this.getClass() );

    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        try
        {

            List list = Service.getServiceNames();

            for ( Object name : list )
            {
                log.info( "Service {}", (String) name );
                try
                {
                    Service service = new Service( name.toString() );

                    log.info( "Service Status: {}", service.getStatusString() );
                }
                catch ( Win32Exception ex )
                {
                    log.error( ex.getMessage() );
                }
            }

            // Service.getServiceNames().forEach((name) -> {
            // try
            // {
            // Service service = new Service(name.toString());
            // log.info("Service: {}", service);
            // }
            // catch (Win32Exception ex) {
            // log.error(ex.getMessage());
            // }
            // });

        }
        catch ( Win32Exception ex )
        {
            //
        }
    }
}
