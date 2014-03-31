package com.securelink.mon4j.jobs;

import org.hyperic.sigar.win32.EventLog;
import org.hyperic.sigar.win32.EventLogRecord;
import org.hyperic.sigar.win32.Win32Exception;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class EventLogJob
    extends BaseJob
{
    public Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Override
    public void execute( JobExecutionContext jec )
        throws JobExecutionException
    {
        // TODO: Get log criticality from props file
        String[] logNames = EventLog.getLogNames();

        for ( String logName : logNames )
        {
            logger.info( "EventLog: {}", logName );

            try
            {
                readAll( logName );
            }
            catch ( Exception ex )
            {
                logger.info( ex.getMessage() );
            }
        }

    }

    private int readAll( String logname )
        throws Exception
    {
        int fail = 0, success = 0, max = 50;
        EventLogRecord record;
        EventLog log = new EventLog();

        log.open( logname );
        if ( log.getNumberOfRecords() == 0 )
        {
            log.close();
            return 0; // else log.getOldestRecord() throws Exception
        }
        int oldestRecord = log.getOldestRecord();
        int numRecords = log.getNumberOfRecords();
        logger.info( "oldest=" + oldestRecord + ", total=" + numRecords + ", max=" + max );

        for ( int i = oldestRecord; i < oldestRecord + numRecords; i++ )
        {
            try
            {
                record = log.read( i );
                logger.info( record.toString() );
                success++;
                if ( success > max )
                {
                    break;
                }
            }
            catch ( Win32Exception e )
            {
                fail++;
                logger.info( "Error reading record " + i + ": " + e.getMessage() );
            }
        }

        log.close();

        logger.info( "success=" + success + ", fail=" + fail );
        return success;
    }
}
