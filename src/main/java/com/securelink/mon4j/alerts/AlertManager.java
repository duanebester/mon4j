package com.securelink.mon4j.alerts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author duanebester
 */
public class AlertManager
{
    private static volatile AlertManager instance = new AlertManager();

    public Logger log = LoggerFactory.getLogger( AlertManager.class );

    // private final Object alertLock = new Object();

    private final Set<Alert> alerts = Collections.synchronizedSet( new HashSet<Alert>() );

    private AlertManager()
    {
    }

    public static AlertManager getInstance()
    {
        if ( instance == null )
        {
            synchronized ( AlertManager.class )
            {
                if ( instance == null )
                {
                    instance = new AlertManager();
                }
            }
        }

        return instance;
    }

    public void processAlert( String category, String info, String key )
    {
        Alert alert = new Alert( category, info, 0, key );
        processAlert( alert );
    }

    public void processAlert( Alert alert )
    {
        log.info( "\n##############################################" );
        log.info( alert.toString() );
        log.info( "##############################################\n" );
        // get prop for serverURL:port
        // Client client = new Client("http://");
        // if ( alerts.contains( alert ) )
        // {
        // // Alter priority? If priority changes, send new alert
        // }
        // else
        // {
        // // Add to list, send alert
        // alert.setCreated( new Date() );
        // alerts.add( alert );
        //
        // }
    }

    public void removeAlert( String key )
    {
        alerts.stream().filter((alert) -> (key.equals( alert.getKey() ) )).forEach((alert) -> {
            alerts.remove(alert);
        });
    }
}
