package com.securelink.mon4j.jobs;

import org.hyperic.sigar.Sigar;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class BaseJob implements Job
{

    /**
     * @return the sigar
     */
    public synchronized Sigar getSigar() {
        return sigar;
    }
    public Logger log = LoggerFactory.getLogger( "jobs" );
    
    private static final Sigar sigar = new Sigar();  
}
