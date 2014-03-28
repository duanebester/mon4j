package com.securelink.mon4j.jobs;

import com.securelink.mon4j.util.Props;
import java.util.Properties;
import org.hyperic.sigar.Sigar;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.PersistJobDataAfterExecution;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class BaseJob
    implements Job
{
    Properties props = Props.getInstance().getProperties();

    private static final Sigar sigar = new Sigar();

    private final Object lock = new Object();

    /**
     * @return the sigar
     */
    public final Sigar getSigar()
    {
        synchronized ( lock )
        {
            return sigar;
        }
    }
}
