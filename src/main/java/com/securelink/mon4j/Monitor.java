package com.securelink.mon4j;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class Monitor 
{
    public static final Logger log = LoggerFactory.getLogger( Monitor.class );
    
    public static void main( String args[] )
    {
        //Engine engine = new Engine();
        
//        Runtime.getRuntime().addShutdownHook( new Thread() 
//        {
//            @Override
//            public void run() 
//            {
//                log.info("Shutting down Engine...");
//                
//                if ( engine != null )
//                {
//                    engine.stop();
//                }
//            }
//        });
        
        Sigar sigar = new Sigar();
        
        Mem mem = null;
        try {
            mem = sigar.getMem();
        } catch (SigarException se) {
            log.error(se.getMessage());
        }
        
        log.info( "Memory :\n {}", mem.toString() );
        
        log.info("Shutting down Monitor, Bye!");
        System.exit(0);
    }

}
