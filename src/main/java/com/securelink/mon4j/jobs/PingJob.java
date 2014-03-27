package com.securelink.mon4j.jobs;

import com.securelink.mon4j.util.Props;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

            boolean reachable = false;
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

                /*
                 * try { //InetAddress inet = InetAddress.getByName( ip ); try { //NetStat stat = getSigar().getNetStat(
                 * inet.getAddress(), 22 ); int p = RPC.ping(ip, 1000); log.info("p {} ip {}", p, ip ); } catch (
                 * SigarException se ) { log.info( se.getMessage() ); } // log.info( ( inet.isReachable( 5000 ) ?
                 * "Host '{}' reachable" : "Host '{}' Not Reachable" ), ip ); } catch ( UnknownHostException ex ) {
                 * log.error( ex.getMessage() ); }
                 */
            }
            catch ( IOException ex )
            {
                reachable = false;
            }

            log.info( ( reachable ? "{} reachable" : "{} not reachable" ), ip );
        }

    }

}
