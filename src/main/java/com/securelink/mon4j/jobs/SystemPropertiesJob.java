package com.securelink.mon4j.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class SystemPropertiesJob
    extends BaseJob
{
    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        // System.getProperties().stringPropertyNames().forEach((prop) -> { log.info("Property {}", prop); });
    }
}
