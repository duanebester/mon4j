package com.securelink.mon4j.jobs;

import org.hyperic.sigar.win32.EventLog;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class EventLogJob 
    extends BaseJob 
{
    public Logger log = LoggerFactory.getLogger( this.getClass() );
    
    @Override
    public void execute(JobExecutionContext jec) 
        throws JobExecutionException 
    {
        String[] logNames = EventLog.getLogNames();
        
        for ( String logName : logNames )
        {
            log.info( "EventLog: {}", logName );
        }
    }
}
