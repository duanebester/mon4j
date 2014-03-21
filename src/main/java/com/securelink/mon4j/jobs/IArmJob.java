package com.securelink.mon4j.jobs;

/**
 *
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */
public interface IArmJob 
{
    public int getArmValue();
    
    public int getReArmValue();
    
    public int getArmDelay();
    
    public String getOperator();
}
