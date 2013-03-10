package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.domain.ServiceMonitorResult;

import java.util.List;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public interface ServiceMonitorManager {

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
    public List<ServiceMonitorResult> getAllMonitorResults();

    /**
     *
     * @param monitorName
     * @return
     */
    public List<ServiceMonitorResult> getMonitorResults(String monitorName);

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
