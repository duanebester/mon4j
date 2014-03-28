package com.securelink.mon4j.jobs;

import java.io.File;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class DiskJob
    extends BaseArmJob
{
    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        setup( jec );

        boolean win = "windows".equals( props.getProperty( "os.name" ) );

        try
        {
            File[] roots = File.listRoots();

            for ( File file : roots )
            {
                String fName = win ? file.getAbsolutePath() + "\\" : file.getAbsolutePath();

                log.info( fName );

                // Only check C Drive if windows
                if ( !win || ( win && fName.contains( "C:" ) ) )
                {
                    FileSystemUsage fsu = getSigar().getFileSystemUsage( fName );

                    if ( "percent".equals( getOperator() ) )
                    {
                        setCurrentValue( fsu.getUsePercent() * 100L );
                    }
                }
            }

        }
        catch ( SigarException ex )
        {
            log.error( ex.getMessage() );
        }

        log.info( "ArmValue {}", getArmValue() );
        log.info( ">>--- DISK --> {}", getCurrentValue() );

        if ( stateProcessor( jec ) == JobState.ALERT )
        {
            throw new JobExecutionException( "Disk has been running over " + getArmValue() + getOperator() + " for " + getArmDelay() + " seconds" );
        }
    }

}
