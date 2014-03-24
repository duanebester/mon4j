package com.securelink.mon4j.alerts;

import com.securelink.mon4j.client.Client;
import com.securelink.mon4j.util.Props;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author duanebester
 */
public class AlertManager
{
    private static volatile AlertManager instance = new AlertManager();

    public Logger log = LoggerFactory.getLogger( AlertManager.class );

    Properties props = Props.getInstance().getProperties();

    // private final Object alertLock = new Object();

    private final List<Alert> alerts = Collections.synchronizedList( new ArrayList<Alert>() );

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
        Alert alert = new Alert( category, info, 1, key );
        processAlert( alert );
    }

    public void processAlert( Alert newAlert )
    {
        log.info( "--Alert" );
        log.info( newAlert.toString() );

        boolean alreadyHaveAlert = false;

        Long time2IncrementMillis = Integer.parseInt( props.getProperty( "incrementPriorityAfter", "3600" ) ) * 1000L;

        for ( Alert alert : alerts )
        {
            if ( alert.getKey().equals( newAlert.getKey() ) )
            {
                log.info( "Alert already in list..." );
                alreadyHaveAlert = true;
                Long now = System.currentTimeMillis();
                Long alertCreated = alert.getCreated().getTime();

                if ( ( alertCreated + time2IncrementMillis ) < now )
                {
                    int currentPriority = alert.getPriority();
                    alert.setPriority( currentPriority + 1 );
                    sendAlert( alert );
                }
            }
        }

        newAlert.setCreated( new Date() );

        if ( !alreadyHaveAlert )
        {
            log.info( "Sending First Alert now!" );
            alerts.add( newAlert );
            sendAlert( newAlert );
        }
    }

    public void removeAlert( String key )
    {
        alerts.stream().filter((alert) -> (key.equals( alert.getKey() ) )).forEach((alert) -> {
            alerts.remove(alert);
        });
    }

    private void sendAlert( Alert newAlert )
    {
        try
        {
            String serverURL = props.getProperty( "alertServerURL", "localhost" );
            String port = props.getProperty( "alertServerPort", "4680" );
            String protocol = "http://";

            String uri = props.getProperty( "alertURI", "/alert.action?info={info}&category={category}&key={key}&priority={priority}&summary=blah" );

            uri =
                uri.replace( "{info}", URLEncoder.encode( newAlert.getInfo(), "UTF-8" ) ).replace( "{category}", URLEncoder.encode( newAlert.getCategory(), "UTF-8" ) ).replace( "{key}",
                                                                                                                                                                                 URLEncoder.encode( newAlert.getKey(),
                                                                                                                                                                                                    "UTF-8" ) ).replace( "{priority}",
                                                                                                                                                                                                                         URLEncoder.encode( String.valueOf( newAlert.getPriority() ),
                                                                                                                                                                                                                                            "UTF-8" ) );

            log.info( "URL: {}", protocol + serverURL + ":" + port + uri );
            URI alertUri = new URI( protocol + serverURL + ":" + port + uri );
            Client client = new Client( alertUri );
            log.info( "-- before client run --" );
            client.run();
            log.info( "-- after client run --" );
        }
        catch ( UnsupportedEncodingException | URISyntaxException | InterruptedException ex )
        {
            log.error( "Couldn't send alert! {}", ex.getMessage() );
        }
    }
}
