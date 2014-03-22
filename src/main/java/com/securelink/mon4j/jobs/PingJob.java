package com.securelink.mon4j.jobs;

import java.io.IOException;
import java.net.InetAddress;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class PingJob
    extends BaseJob
{

    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        try
        {
            InetAddress inet = InetAddress.getByName( "127.0.0.1" );
            log.info( inet.isReachable( 5000 ) ? "Host reachable" : "Host Not Reachable" );
        }
        catch ( IOException ex )
        {
            log.error( ex.getMessage() );
        }

    }

}
