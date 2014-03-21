package com.securelink.mon4j;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class Engine 
{
    private final Logger log = LoggerFactory.getLogger( Engine.class );
    
    private Scheduler scheduler;
    
    public boolean start()
    {
        try 
        {
            // Grab the Scheduler instance from the Factory 
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            
            // start it off
            scheduler.start();

        } catch (SchedulerException se) 
        {
            log.error( se.getMessage() );
            return false;
        }
        log.info( "Engine started" );
        return true;
    }
    
    public void stop()
    {
        try 
        {
            if ( scheduler != null )
            {
                scheduler.shutdown();
            }
        } 
        catch (SchedulerException ex) 
        {
            log.error( ex.getMessage() );
        }
        
        log.info( "Engine shutdown" );
    }
}
