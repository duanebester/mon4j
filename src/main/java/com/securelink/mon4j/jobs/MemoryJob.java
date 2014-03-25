package com.securelink.mon4j.jobs;

import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class MemoryJob
    extends BaseArmJob
{
    public Logger log = LoggerFactory.getLogger( MemoryJob.class );

    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        setup( jec );

        try
        {
            setCurrentValue( getSigar().getMem().getUsedPercent() );
        }
        catch ( SigarException se )
        {
            log.error( se.getMessage() );
        }

        log.info( "ArmValue {}", getArmValue() );
        log.info( ">>--- MEMORY --> {}", getCurrentValue() );

        if ( stateProcessor( jec ) == JobState.ALERT )
        {
            throw new JobExecutionException( "Memory usage has been over " + getArmValue() + getOperator() + " for " + getArmDelay() + " seconds" );
        }
    }
}
