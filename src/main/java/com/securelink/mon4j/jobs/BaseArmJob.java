package com.securelink.mon4j.jobs;

import java.util.Date;


/**
 *
 * @author duanebester
 */
public abstract class BaseArmJob extends BaseJob implements IArmJob 
{
    private JobState state = JobState.NORMAL;
    
    private Date timer;
    
    public JobState getState()
    {
        switch(state) 
        {
            case NORMAL :
                if ( getCurrentValue() > getArmValue() )
                {
                    state = JobState.PENDING;
                    timer = new Date();
                }
                break;
            case PENDING :
                Date now = new Date();
                Long delay = (long) getArmDelay() * 1000;
                if ( getCurrentValue() < getReArmValue() )
                {
                    state = JobState.NORMAL;
                    timer = new Date();
                }
                else if ( now.getTime() < ( timer.getTime() + delay ) )
                {
                    state = JobState.ALERT;
                }
                break;
            case ALERT :
                if ( getCurrentValue() < getReArmValue() )
                {
                    state = JobState.ALERT;
                }
                break;
            default :
                state = JobState.NORMAL;
                timer = new Date();
                break;
                
        }
        
        return state;
    }
}
