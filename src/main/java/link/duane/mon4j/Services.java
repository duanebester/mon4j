package link.duane.mon4j;

import link.duane.mon4j.services.IService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 * @param <S> S implements IService
 */

public class Services<S extends IService>
{
    private static volatile Services instance = new Services();

    private final List<S> services = Collections.synchronizedList( new ArrayList<S>() );

    private Services()
    {
    }

    public static Services getInstance()
    {
        if ( instance == null )
        {
            synchronized ( Services.class )
            {
                if ( instance == null )
                {
                    instance = new Services();
                }
            }
        }

        return instance;
    }

    public void addService( S service )
    {
        services.add( service );
    }

    /**
     * @return the services
     */
    public List<S> getServices()
    {
        return services;
    }
}
