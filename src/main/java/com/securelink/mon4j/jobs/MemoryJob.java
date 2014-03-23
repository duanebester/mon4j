package com.securelink.mon4j.jobs;

import org.hyperic.sigar.Mem;
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

        Mem mem = null;
        try
        {
            mem = getSigar().getMem();
        }
        catch ( SigarException se )
        {
            log.error( se.getMessage() );
        }

        log.info( ">>--- Memory --> {}", mem.toString() );
    }

}
