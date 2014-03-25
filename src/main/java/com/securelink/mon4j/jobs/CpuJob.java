package com.securelink.mon4j.jobs;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class CpuJob
    extends BaseArmJob
{
    public Logger log = LoggerFactory.getLogger( CpuJob.class );

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
            log.error( ex.getMessage() );
            // Alert here?
            setCurrentValue( -1 );
        }

        log.info( "ArmValue {}", getArmValue() );
        log.info( ">>--- CPU --> {}", getCurrentValue() );

        if ( stateProcessor( jec ) == JobState.ALERT )
        {
            throw new JobExecutionException( "CPU has been running over " + getArmValue() + getOperator() + " for " + getArmDelay() + " seconds" );
        }
    }
}