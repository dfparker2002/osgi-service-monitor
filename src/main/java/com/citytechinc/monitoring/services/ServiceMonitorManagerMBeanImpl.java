package com.citytechinc.monitoring.services;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import java.util.List;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(immediate = true)
@Property(name = "jmx.objectname", value = "com.citytechinc.monitoring.services:type=OSGi Service Monitor")
@Service
public final class ServiceMonitorManagerMBeanImpl implements ServiceMonitorManagerMBean {

    @Reference
    ServiceMonitorManager serviceMonitorManager;

    @Override
    public void poll() {
        serviceMonitorManager.poll();
    }

    @Override
    public List<String> getMonitors() {
        return serviceMonitorManager.listMonitors();
    }

    @Override
    public List<String> getNotificationDeliveryAgents() {
        return serviceMonitorManager.listNotificationDeliveryAgents();
    }

    @Override
    public List<String> getAlarmedMonitors() {
        return serviceMonitorManager.getAlarmedMonitors();
    }

    @Override
    public void resetAllAlarms() {
        serviceMonitorManager.resetAllAlarms();
    }

    @Override
    public void resetAlarm(final String monitorName) {
        serviceMonitorManager.resetAlarm(monitorName);
    }

}
