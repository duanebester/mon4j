package com.securelink.mon4j.jobs;

import com.securelink.mon4j.util.Props;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.RPC;
import org.hyperic.sigar.SigarException;
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
            try
            {
                ip = ip.trim();
                InetAddress inet = InetAddress.getByName( ip );

                try
                {
                    NetStat stat = getSigar().getNetStat( inet.getAddress(), 22 );
                    int p = RPC.ping(ip, 1000);

                    log.info("stat {} p {} ip {}", stat.getTcpStates(), p, ip );
                }
                catch ( SigarException se )
                {
                    log.info( se.getMessage() );
                }

                // log.info( ( inet.isReachable( 5000 ) ? "Host '{}' reachable" : "Host '{}' Not Reachable" ), ip );
            }
            catch ( UnknownHostException ex )
            {
                log.error( ex.getMessage() );
            }
        }

    }

}
