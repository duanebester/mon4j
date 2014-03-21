package com.securelink.mon4j.jobs;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class CpuJob extends BaseJob 
{
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException 
    {
        Cpu cpu = null;
        try {
            cpu = sigar.getCpu();
        } catch (SigarException se) {
            log.error(se.getMessage());
        }

        log.info( "CPU :\n {}", cpu.toString() );
    }
}
