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
            // TODO: Get list of services from properties file
            List list = Service.getServiceNames();

            for ( Object name : list )
            {
                try
                {
                    Service service = new Service( name.toString() );

                    log.info( "Service Name: {} Status: {}", name.toString(), service.getStatusString() );
                }
                catch ( Win32Exception ex )
                {
                    // Unable to open service
                    log.error( ex.getMessage() );
                }
            }
        }
        catch ( Win32Exception ex )
        {
            // Unable to get Service Names
            log.error( ex.getMessage() );
        }
    }
}
