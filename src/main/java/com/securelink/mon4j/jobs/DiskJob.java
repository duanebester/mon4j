package com.securelink.mon4j.jobs;

import java.io.File;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class DiskJob extends BaseJob 
{

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException 
    {
        try 
        {
            File[] roots = File.listRoots();
            
            for ( File file : roots )
            {
                log.info("Disk: {}", sigar.getDiskUsage( file.getAbsolutePath() ).toString());
            }

        } catch (SigarException ex) {
            log.error(ex.getMessage());
        }
        
    }
    
}
