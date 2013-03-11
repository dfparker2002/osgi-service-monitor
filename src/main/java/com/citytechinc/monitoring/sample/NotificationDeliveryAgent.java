package com.citytechinc.monitoring.sample;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.domain.ServiceMonitorResult;
import com.citytechinc.monitoring.domain.SubscriptionDefinition;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(label = "Sample Notification Delivery Agent", description = "")
@Service
@Properties({
        @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = Constants.CITYTECH_SERVICE_VENDOR_NAME) })
public final class NotificationDeliveryAgent implements com.citytechinc.monitoring.services.NotificationDeliveryAgent {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationDeliveryAgent.class);

    @Override
    public void notify(final ServiceMonitorResult serviceMonitorResult) {

        LOG.debug("Received ServiceMonitorResult: '{}'", serviceMonitorResult);
    }

    @Override
    public SubscriptionDefinition getSubscriptionDefinition() {
        return SubscriptionDefinition.ALL_MONITORS();
    }

}
