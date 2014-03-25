package com.securelink.mon4j.jobs;

import com.securelink.mon4j.util.Props;
import java.io.IOException;
import java.net.InetAddress;
import org.apache.commons.lang3.StringUtils;
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
        String ips = Props.getInstance().getProperties().getProperty( "pingURL" );

        for ( String ip : StringUtils.split( ips, "," ) )
        {
            try
            {
                InetAddress inet = InetAddress.getByName( ip );
                log.info( ( inet.isReachable( 5000 ) ? "Host {} reachable" : "Host {} Not Reachable" ), ip );
            }
            catch ( IOException ex )
            {
                log.error( ex.getMessage() );
            }
        }

    }

}
