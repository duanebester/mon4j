package com.securelink.mon4j.jobs;

import com.securelink.mon4j.util.Props;
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
                boolean win = "windows".equals( Props.getInstance().getProperties().getProperty("os.name"));
                
                String fName = win ? file.getAbsolutePath() + "\\" : file.getAbsolutePath();
                
                log.info(fName);
                
                // Only check C Drive
                if ( win && fName.contains("C:") )
                {
                    log.info("{} Disk Usage: {}", fName, sigar.getDiskUsage( fName ).toString() );
                    log.info("{} FileSystem Usage: {}", fName, sigar.getFileSystemUsage( fName ).toString() );
                }
                else
                {
                    log.info("{} Disk Usage: {}", fName, sigar.getDiskUsage( fName ).toString() );
                    log.info("{} FileSystem Usage: {}", fName, sigar.getFileSystemUsage( fName ).toString() );
                }
            }

        } catch (SigarException ex) {
            log.error(ex.getMessage());
        }
        
    }
    
}
