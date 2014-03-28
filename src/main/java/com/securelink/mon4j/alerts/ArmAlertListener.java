package com.securelink.mon4j.alerts;

import static com.securelink.mon4j.jobs.IArmJob.ARM_DELAY;
import static com.securelink.mon4j.jobs.IArmJob.ARM_VALUE;
import static com.securelink.mon4j.jobs.IArmJob.JOB_STATE;
import static com.securelink.mon4j.jobs.IArmJob.OPERATOR;
import static com.securelink.mon4j.jobs.IArmJob.RE_ARM_VALUE;
import com.securelink.mon4j.jobs.JobState;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author duanebester
 */
public class ArmAlertListener
    implements JobListener
{

    public Logger log = LoggerFactory.getLogger( ArmAlertListener.class );

    AlertManager alertManager = AlertManager.getInstance();

    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted( JobExecutionContext jec )
    {
        log.info( "{} About to be executed", jec.getJobDetail().getKey().getName() );
    }

    @Override
    public void jobWasExecuted( JobExecutionContext jec, JobExecutionException jee )
    {
        JobDetail detail = jec.getJobDetail();
        JobDataMap jdm = detail.getJobDataMap();

        log.info( "{} was executed", detail.getKey().getName() );

        if ( jdm.get( JOB_STATE ) != null && jdm.get( JOB_STATE ) == JobState.ALERT )
        {
            String category = getCategory( detail.getJobClass() );
            String jobKey = detail.getKey().toString();
            String info = category + " above " + jdm.getDouble( ARM_VALUE ) + " " + jdm.getString( OPERATOR );
            String summary =
                category + " has been above " + jdm.getDouble( ARM_VALUE ) + " " + jdm.getString( OPERATOR ) + " for " + jdm.getInt( ARM_DELAY ) + " seconds. Never dropped below "
                    + jdm.getDouble( RE_ARM_VALUE );

            alertManager.processAlert( category, info, jobKey, summary );
        }

        if ( jee != null )
        {
            log.error( "Exception thrown by: {} Exception: {}", jec.getJobDetail().getKey(), jee.getMessage() );
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

    @Override
    public void jobExecutionVetoed( JobExecutionContext jec )
    {
        log.error( "Job was vetoed" );
    }

}
