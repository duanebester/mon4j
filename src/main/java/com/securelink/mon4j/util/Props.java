package com.securelink.mon4j.util;

import java.util.Properties;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class Props
{
    private static volatile Props instance = new Props();

    private Properties properties;

    private Props()
    {
    }

    public static Props getInstance()
    {
        if ( instance == null )
        {
            synchronized ( Props.class )
            {
                if ( instance == null )
                {
                    instance = new Props();
                }
            }
        }

        return instance;
    }

    /**
     * @return the properties
     */
    public Properties getProperties()
    {
        synchronized ( this )
        {
            return properties;
        }
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties( Properties properties )
    {
        synchronized ( this )
        {
            this.properties = properties;
        }
    }
}