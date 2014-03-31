package com.securelink.mon4j.jobs;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class CpuJob
    extends BaseArmJob
{
    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        setup( jec );

        CpuPerc perc;
        try
        {
            perc = getSigar().getCpuPerc();
            // log.info( "System={}", perc.getSys() );
            // log.info( "User={}", perc.getUser() );
            // log.info( "Combined={}", perc.getCombined() );
            setCurrentValue( perc.getCombined() * 100L );
        }
        catch ( SigarException ex )
        {
            // Alert here?
            setCurrentValue( -1 );
        }

        stateProcessor( jec );
    }
}