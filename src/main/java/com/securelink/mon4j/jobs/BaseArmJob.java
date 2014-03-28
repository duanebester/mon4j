package com.securelink.mon4j.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author duanebester
 */
public abstract class BaseArmJob
    extends BaseJob
    implements IArmJob
{
    private double armValue;

    private double reArmValue;

    private int armDelay;

    private double currentValue;

    private String operator;

    private JobState state;

    private Long time2TriggerAlarm;

    private Long thresholdWait;

    private Long now;

    @Override
    public void setup( JobExecutionContext jec )
    {
        JobDataMap jdm = jec.getJobDetail().getJobDataMap();

        setArmValue( jdm.getDouble( ARM_VALUE ) );

        setReArmValue( jdm.getDouble( RE_ARM_VALUE ) );

        setArmDelay( jdm.getInt( ARM_DELAY ) );

        setOperator( jdm.getString( OPERATOR ) );

        state = ( jdm.get( JOB_STATE ) == null ) ? JobState.NORMAL : (JobState) jdm.get( JOB_STATE );

        time2TriggerAlarm = ( jdm.get( TIME_TO_TRIGGER ) == null ) ? 0 : (long) jdm.get( TIME_TO_TRIGGER );

        thresholdWait = getArmDelay() * 1000L;
    }

    @Override
    public JobState stateProcessor( JobExecutionContext jec )
        throws JobExecutionException // State engine processor.
    {
        JobDataMap jdm = jec.getJobDetail().getJobDataMap();
        // precalc current time (now)
        now = System.currentTimeMillis();

        switch ( getState() )
        {
            case NORMAL:
                if ( getCurrentValue() >= getArmValue() )
                {
                    state = JobState.PENDING;
                    // precalc of time2TriggerAlarm will help speed up processing
                    time2TriggerAlarm = now + thresholdWait; // time in future
                    jdm.put( TIME_TO_TRIGGER, time2TriggerAlarm );
                }
                break;
            case PENDING:
                if ( getCurrentValue() < getReArmValue() )
                {
                    state = JobState.NORMAL;
                }
                else if ( now > time2TriggerAlarm ) // While less, stay in pending  
                {
                    state = JobState.ALERT;
                }
                break;
            case ALERT:
                if ( getCurrentValue() < getReArmValue() )
                {
                    state = JobState.NORMAL; // NORMAL
                }
                break;
            default:
                state = JobState.NORMAL;
        }

        jdm.put( JOB_STATE, state );

        return state;
    }

    /**
     * @return the armValue
     */
    @Override
    public double getArmValue()
    {
        return armValue;
    }

    /**
     * @param armValue the armValue to set
     */
    public void setArmValue( double armValue )
    {
        this.armValue = armValue;
    }

    /**
     * @return the reArmValue
     */
    @Override
    public double getReArmValue()
    {
        return reArmValue;
    }

    /**
     * @param reArmValue the reArmValue to set
     */
    public void setReArmValue( double reArmValue )
    {
        this.reArmValue = reArmValue;
    }

    /**
     * @return the armDelay
     */
    @Override
    public int getArmDelay()
    {
        return armDelay;
    }

    /**
     * @param armDelay the armDelay to set
     */
    public void setArmDelay( int armDelay )
    {
        this.armDelay = armDelay;
    }

    /**
     * @return the currentValue
     */
    @Override
    public double getCurrentValue()
    {
        return currentValue;
    }

    /**
     * @param currentValue the currentValue to set
     */
    public void setCurrentValue( double currentValue )
    {
        this.currentValue = currentValue;
    }

    /**
     * @return the operator
     */
    @Override
    public String getOperator()
    {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    /**
     * @return the state
     */
    public JobState getState()
    {
        return state;
    }
}
