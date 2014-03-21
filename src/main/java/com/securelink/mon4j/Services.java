package com.securelink.mon4j;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class Services 
{
    private static volatile Services instance = new Services();
    
    private Services() {}
    
    public static Services getInstance() 
    {
        if ( instance == null )
        {
            synchronized( Services.class )
            {
                if ( instance == null )
                {
                    instance = new Services();
                }
            }
        }
        
        return instance;
    }
}
