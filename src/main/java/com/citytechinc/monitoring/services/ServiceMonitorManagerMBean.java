package com.citytechinc.monitoring.services;

import java.util.List;

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
    public List<String> listMonitors();

    /**
     *
     * @return
     */
    public List<String> listNotificationDeliveryAgents();

    /**
     *
     * @return
     */
    public List<String> getAlarmedMonitors();

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
