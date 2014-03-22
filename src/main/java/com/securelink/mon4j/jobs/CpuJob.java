package com.securelink.mon4j.jobs;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class CpuJob extends BaseArmJob 
{
    private int armValue;
    
    private int reArmValue;
    
    private int armDelay;
    
    private int currentValue;
    
    private String operator = "percent";
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException 
    {
        JobDataMap jdm = jec.getJobDetail().getJobDataMap();
        
        armValue = jdm.getInt( ARM_VALUE );
        
        reArmValue = jdm.getInt( RE_ARM_VALUE );
        
        armDelay = jdm.getInt( ARM_DELAY );
        
        operator = jdm.getString( OPERATOR );
        
        CpuPerc perc;
        try 
        {
            perc = getSigar().getCpuPerc();
            log.info( "System={}", perc.getSys() );
            log.info( "User={}", perc.getUser() );
            currentValue = (int) perc.getSys();
        } 
        catch (SigarException ex) 
        {
            log.error(ex.getMessage()); 
            // Alert here?
            currentValue = -1;
        }
        
        log.info( "State: {}", getState() );
        log.info( "ArmValue {}", getArmValue() );
        log.info( ">>--- CPU --> {}", currentValue );
    }

    /**
     * @return the armValue
     */
    @Override
    public int getArmValue() {
        return armValue;
    }

    /**
     * @param armValue the armValue to set
     */
    public void setArmValue(int armValue) {
        this.armValue = armValue;
    }

    /**
     * @return the reArmValue
     */
    @Override
    public int getReArmValue() {
        return reArmValue;
    }

    /**
     * @param reArmValue the reArmValue to set
     */
    public void setReArmValue(int reArmValue) {
        this.reArmValue = reArmValue;
    }

    /**
     * @return the armDelay
     */
    @Override
    public int getArmDelay() {
        return armDelay;
    }

    /**
     * @param armDelay the armDelay to set
     */
    public void setArmDelay(int armDelay) {
        this.armDelay = armDelay;
    }

    /**
     * @return the currentValue
     */
    @Override
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * @param currentValue the currentValue to set
     */
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * @return the operator
     */
    @Override
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    
    
}
