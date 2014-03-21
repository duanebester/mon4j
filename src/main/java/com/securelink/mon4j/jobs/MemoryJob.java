package com.securelink.mon4j.jobs;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class MemoryJob implements Job {

    public final Logger log = LoggerFactory.getLogger( MemoryJob.class );
    
    @Override
    public void execute( JobExecutionContext jec ) throws JobExecutionException 
    {
        Sigar sigar = new Sigar();
        
        Mem mem = null;
        try {
            mem = sigar.getMem();
        } catch (SigarException se) {
            log.error(se.getMessage());
        }
        
        log.info( "Memory :\n {}", mem.toString() );
    }

}
