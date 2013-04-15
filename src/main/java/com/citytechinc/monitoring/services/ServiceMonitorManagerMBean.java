package com.citytechinc.monitoring.services;

import javax.management.openmbean.TabularDataSupport;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public interface ServiceMonitorManagerMBean {

    /**
     *
     */
    public void poll();

    /**
     *
     * @return
     */
    public TabularDataSupport getMonitors();

    /**
     *
     * @return
     */
    public TabularDataSupport getNotificationDeliveryAgents();

    /**
     *
     * @return
     */
    public TabularDataSupport getAlarmedMonitors();

    /**
     *
     */
    public void resetAllAlarms();

    /**
     *
     * @param monitorName
     */
    public void resetAlarm(String monitorName);

}
