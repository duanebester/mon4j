package com.securelink.mon4j.jobs;

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

public class SystemPropertiesJob implements Job 
{
    public final Logger log = LoggerFactory.getLogger( SystemPropertiesJob.class );
    
    @Override
    public void execute( JobExecutionContext jec ) throws JobExecutionException 
    {
        System.getProperties().stringPropertyNames().forEach((prop) -> { log.info("Property {}", prop); });
    }
}
