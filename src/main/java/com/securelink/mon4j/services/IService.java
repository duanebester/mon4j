package com.securelink.mon4j.services;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */
public interface IService
{
    public static final String ARM_VALUE = "armValue";

    public static final String CURRENT_VALUE = "currentValue";

    public static final String RE_ARM_VALUE = "reArmValue";

    public static final String ARM_DELAY = "armDelay";

    public static final String OPERATOR = "operator";

    public static final String IP_ADDRESSES = "ipAddresses";

    public static final String FAIL_COUNT = "pingFailCount";

    public static final String SERVICE_DOWN_TIME = "timeDownSeconds";

    public JobDetail getJob();

    public Trigger getTrigger();
}
