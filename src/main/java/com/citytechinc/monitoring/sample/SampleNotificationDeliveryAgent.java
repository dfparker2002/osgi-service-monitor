package com.citytechinc.monitoring.sample;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.domain.ServiceMonitorResult;
import com.citytechinc.monitoring.domain.SubscriptionDefinition;
import com.citytechinc.monitoring.services.NotificationDeliveryAgent;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This NotificationDeliveryAgent simply logs whenever it is called.
 *
 * There are two ServiceMonitors in this sample package. One of then always returns "SUCCESS" while the other
 *   (SometimesSuccessfulServiceMonitor) randomly returns "UNEXPECTED_SERVICE_RESPONSE". Unless a configuration overrides
 *   the default, the ServiceMonitorManager defaults to requiring 3 sequential failures for any given ServiceMonitor to
 *   trigger NotificationDeliveryAgent(s). Therefore, it might take a few rounds of poll requests for notify(...) to
 *   be invoked.
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(label = "Sample Notification Delivery Agent", description = "")
@Service
@Properties({
        @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = Constants.CITYTECH_SERVICE_VENDOR_NAME) })
public final class SampleNotificationDeliveryAgent implements NotificationDeliveryAgent {

    private static final Logger LOG = LoggerFactory.getLogger(SampleNotificationDeliveryAgent.class);

    @Override
    public void notify(final ServiceMonitorResult serviceMonitorResult) {

        LOG.error("Received ServiceMonitorResult: '{}'", serviceMonitorResult);
    }

    @Override
    public SubscriptionDefinition getSubscriptionDefinition() {

        // SUBSCRIBE TO THE ALWAYS SUCCESSFUL MONITOR AND THEREFORE, BASED ON THE SAMPLE, NEVER BE TRIGGERED
        // return SubscriptionDefinition.SUBSCRIBE_TO_SPECIFIC_MONITORS(Lists.newArrayList(new String[] {"com.citytechinc.monitoring.sample.SuccessfulServiceMonitor"}));

        // YOU CAN ALSO SUBSCRIBE TO ALL MONITORS BUT THOSE DEFINED IN YOUR LIST
        // return SubscriptionDefinition.UNSUBSCRIBE_FROM_SPECIFIC_MONITORS(Lists.newArrayList(new String[] {"com.citytechinc.monitoring.sample.SometimesSuccessfulServiceMonitor"}));

        // SUBSCRIBE TO ALL MONITORS
        return SubscriptionDefinition.SUBSCRIBE_TO_ALL_MONITORS();
    }

}
