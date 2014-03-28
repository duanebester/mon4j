package com.securelink.mon4j.jobs;

import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class MemoryJob
    extends BaseArmJob
{
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
            //
        }

        if ( stateProcessor( jec ) == JobState.ALERT )
        {
            throw new JobExecutionException( "Memory usage has been over " + getArmValue() + getOperator() + " for " + getArmDelay() + " seconds" );
        }

        stateProcessor( jec );
    }
}
