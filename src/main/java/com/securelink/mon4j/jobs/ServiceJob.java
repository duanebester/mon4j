package com.securelink.mon4j.jobs;

import org.hyperic.sigar.win32.Service;
import org.hyperic.sigar.win32.Win32Exception;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class ServiceJob extends BaseJob
{
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException 
    {
        try {
            Service.getServiceNames().forEach((name) -> {
                try
                {
                    Service service = new Service(name.toString());
                    log.info("Service: {}, Status: {}", name.toString(), service.getStatus());
                }
                catch (Win32Exception ex) {
                    log.error(ex.getMessage());
                }
            });
        } catch (Win32Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
