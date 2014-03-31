package com.securelink.mon4j.jobs;

import static com.securelink.mon4j.services.IService.IP_ADDRESSES;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
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
        JobDataMap jdm = jec.getJobDetail().getJobDataMap();

        String ips = jdm.getString( IP_ADDRESSES );

        String[] _ips = StringUtils.split( ips, "," );

        for ( String ip : _ips )
        {
            boolean reachable;
            ip = ip.trim();

            try
            {
                if ( !InetAddress.getByName( ip ).isReachable( 3000 ) )
                {
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
        }

    }

}
