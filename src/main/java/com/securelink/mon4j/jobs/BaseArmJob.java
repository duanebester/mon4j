package com.securelink.mon4j.jobs;


/**
 *
 * @author duanebester
 */
public abstract class BaseArmJob extends BaseJob implements IArmJob 
{
    private JobState state = JobState.NORMAL;
    private Long time2TriggerAlarm;
    private final Long thresholdWait = getArmDelay()*1000L; //Why not store ArmDelay as milliseconds?
    private Long now;


    public JobState stateProcessor() // State engine processor.
    {
        //precalc current time (now)
        now = System.currentTimeMillis();

        switch(state) 
        {
            case NORMAL :
                if ( getCurrentValue() >= getArmValue() )
                {
                    state = JobState.PENDING;
                    //precalc of time2TriggerAlarm will help speed up processing
                    time2TriggerAlarm = System.currentTimeMillis() + thresholdWait; //time in future
                }
                break;
            case PENDING :
                if ( getCurrentValue() < getReArmValue() )
                {
                    state = JobState.NORMAL;
                }
                else if ( now > time2TriggerAlarm ) // While less, stay in pending  
                {
                    state = JobState.ALERT;
                }
                break;
            case ALERT :
                if ( getCurrentValue() < getReArmValue() )
                {
                    state = JobState.NORMAL; //NORMAL
                }
                break;
            default :
                state = JobState.NORMAL;
        }
        
        return state;
    }
}
