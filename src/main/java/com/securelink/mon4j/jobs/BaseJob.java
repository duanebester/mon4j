package com.securelink.mon4j.jobs;

import org.hyperic.sigar.Sigar;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public abstract class BaseJob implements Job
{
    public Logger log = LoggerFactory.getLogger("Job");
    
    public static Sigar sigar = new Sigar();
}
