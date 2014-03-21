package com.securelink.mon4j.services;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 *
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */
public interface IService {
    
    public JobDetail getJob();
    
    public Trigger getTrigger();
    
}
