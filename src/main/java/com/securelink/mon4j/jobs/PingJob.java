package com.securelink.mon4j.jobs;

import com.securelink.mon4j.util.Props;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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
        String ips = Props.getInstance().getProperties().getProperty( "ping.ips" );

        String[] _ips = StringUtils.split( ips, "," );

        for ( String ip : _ips )
        {

            boolean reachable;
            ip = ip.trim();

            try
            {

                if ( !InetAddress.getByName( ip ).isReachable( 3000 ) )
                {
                    log.info( "Trying Socket to port" );

                    Socket socket = new Socket( ip, 80 );

                    reachable = true;
                }
                else
                {
                    reachable = true;
                }
            }
            catch ( IOException ex )
            {
                reachable = false;
            }

            log.info( ( reachable ? "{} reachable" : "{} not reachable" ), ip );
        }

    }

}
