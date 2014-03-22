package com.securelink.mon4j.jobs;

/**
 *
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */
public interface IArmJob 
{
    public static final String ARM_VALUE = "armValue";
    
    public static final String CURRENT_VALUE = "currentValue";
    
    public static final String RE_ARM_VALUE = "reArmValue";
    
    public static final String ARM_DELAY = "armDelay";
    
    public static final String OPERATOR = "operator";
    
    public int getArmValue();
    
    public int getCurrentValue();
    
    public int getReArmValue();
    
    public int getArmDelay();
    
    public String getOperator();
}
