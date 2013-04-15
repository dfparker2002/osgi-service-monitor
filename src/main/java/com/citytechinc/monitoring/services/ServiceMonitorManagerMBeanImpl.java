package com.citytechinc.monitoring.services;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(immediate = true)
@Property(name = "jmx.objectname", value = "com.citytechinc.monitoring.services:type=OSGi Service Monitor")
@Service
public final class ServiceMonitorManagerMBeanImpl implements ServiceMonitorManagerMBean {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMonitorManagerMBeanImpl.class);

    @Reference
    ServiceMonitorManager serviceMonitorManager;

    @Override
    public void poll() {
        serviceMonitorManager.poll();
    }

    @Override
    public TabularDataSupport getMonitors() {

        TabularDataSupport tabularDataSupport = null;

        try {

            final String[] itemNames = { "monitorName" };
            final String[] itemDescriptions = { "Monitor Name" };
            final OpenType[] itemTypes = { SimpleType.STRING };

            final String[] indexNames = { "monitorName" };

            final CompositeType pageType = new CompositeType("page", "Page size info", itemNames, itemDescriptions, itemTypes);
            final TabularType pageTabularType = new TabularType("pages", "List of Monitors", pageType, indexNames);
            tabularDataSupport = new TabularDataSupport(pageTabularType);

            for (final String monitorName : serviceMonitorManager.listMonitors()) {
                tabularDataSupport.put(new CompositeDataSupport(pageType, itemNames, new Object[] { monitorName }));
            }

        } catch (final Exception exception) {

            LOG.error("An exception occurred building the TabularDataSupport listing the ServiceMonitor(s)", exception);
        }

        return tabularDataSupport;
    }

    @Override
    public TabularDataSupport getNotificationDeliveryAgents() {

        TabularDataSupport tabularDataSupport = null;

        try {

            final String[] itemNames = { "notificationDeliveryAgent" };
            final String[] itemDescriptions = { "Notification Delivery Agent" };
            final OpenType[] itemTypes = { SimpleType.STRING };

            final String[] indexNames = { "notificationDeliveryAgent" };

            final CompositeType pageType = new CompositeType("page", "Page size info", itemNames, itemDescriptions, itemTypes);
            final TabularType pageTabularType = new TabularType("pages", "List of Notification Delivery Agents", pageType, indexNames);
            tabularDataSupport = new TabularDataSupport(pageTabularType);

            for (final String notificationDeliveryAgent : serviceMonitorManager.listNotificationDeliveryAgents()) {
                tabularDataSupport.put(new CompositeDataSupport(pageType, itemNames, new Object[] { notificationDeliveryAgent }));
            }

        } catch (final Exception exception) {

            LOG.error("An exception occurred building the TabularDataSupport listing the NotificationDeliveryAgent(s).", exception);
        }

        return tabularDataSupport;
    }

    @Override
    public TabularDataSupport getAlarmedMonitors() {

        TabularDataSupport tabularDataSupport = null;

        try {

            final String[] itemNames = { "alarmedMonitor" };
            final String[] itemDescriptions = { "Alarmed Monitor" };
            final OpenType[] itemTypes = { SimpleType.STRING };

            final String[] indexNames = { "alarmedMonitor" };

            final CompositeType pageType = new CompositeType("page", "Page size info", itemNames, itemDescriptions, itemTypes);
            final TabularType pageTabularType = new TabularType("pages", "List of Monitors", pageType, indexNames);
            tabularDataSupport = new TabularDataSupport(pageTabularType);

            for (final String alarmedMonitor : serviceMonitorManager.getAlarmedMonitors()) {
                tabularDataSupport.put(new CompositeDataSupport(pageType, itemNames, new Object[] { alarmedMonitor }));
            }

        } catch (final Exception exception) {

            LOG.error("An exception occurred building the TabularDataSupport listing the alarmed ServiceMonitor(s)", exception);
        }

        return tabularDataSupport;
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
