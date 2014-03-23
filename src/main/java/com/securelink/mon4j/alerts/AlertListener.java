package com.securelink.mon4j.alerts;

import com.securelink.mon4j.jobs.CpuJob;
import java.util.concurrent.Future;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author duanebester
 */
public class AlertListener
    implements JobListener
{

    public Logger log = LoggerFactory.getLogger( AlertListener.class );

    AlertManager alertManager = AlertManager.getInstance();

    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted( JobExecutionContext jec )
    {
        log.info( jec.getJobDetail().getKey().getName() );
    }

    @Override
    public void jobExecutionVetoed( JobExecutionContext jec )
    {
        log.error( "Vetoed?" );
    }

    @Override
    public void jobWasExecuted( JobExecutionContext jec, JobExecutionException jee )
    {
        if ( !jee.getMessage().equals( "" ) )
        {
            String jobKey = jec.getJobDetail().getKey().toString();
            String info = jee.getMessage();
            String category = getCategory( jec.getJobDetail().getJobClass() );
            log.error( "Exception thrown by: {} Exception: {}", jobKey, jee.getMessage() );

            alertManager.processAlert( category, info, jobKey );
        }
    }

    public String getCategory( Class clazz )
    {
        String category = "";
        switch ( clazz.getSimpleName() )
        {
            case "CpuJob":
                category = "CPU";
                break;
            case "DiskJob":
                category = "Disk";
                break;
            case "MemoryJob":
                category = "Memory";
                break;
            case "ServiceJob":
                category = "Service";
                break;
            case "PingJob":
                category = "Ping";
                break;
            case "EventLogJob":
                category = "EventLog";
                break;
        }

        return category;
    }

}
