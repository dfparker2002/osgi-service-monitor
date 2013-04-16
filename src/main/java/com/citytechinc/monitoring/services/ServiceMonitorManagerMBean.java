package com.citytechinc.monitoring.services;

import com.adobe.granite.jmx.annotation.Description;
import com.adobe.granite.jmx.annotation.Name;

import javax.management.openmbean.TabularDataSupport;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Description("CITYTECH OSGi Service Monitor Management")
public interface ServiceMonitorManagerMBean {

    /**
     *
     */
    @Description("Instruct the ServiceMonitorManager to poll all monitors")
    public void poll();

    /**
     *
     * @return
     */
    @Description("List all registered ServiceMonitor(s)")
    public TabularDataSupport getMonitors();

    /**
     *
     * @return
     */
    @Description("List all registered NotificationDeliveryAgent(s)")
    public TabularDataSupport getNotificationDeliveryAgents();

    /**
     *
     * @return
     */
    @Description("List all alarmed ServiceMonitor(s)")
    public TabularDataSupport getAlarmedMonitors();

    /**
     *
     */
    @Description("Reset all alarmed ServiceMonitor(s)")
    public void resetAllAlarms();

    /**
     *
     * @param monitorName
     */
    @Description("Reset a specific alarmed ServiceMonitor")
    public void resetAlarm(@Name("monitorName") @Description("The fully qualified path of a ServiceMonitor listed in registered ServiceMonitor(s)") String monitorName);

}
