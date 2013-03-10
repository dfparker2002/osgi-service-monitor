package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.domain.SubscriptionDefinition;
import com.citytechinc.monitoring.domain.ServiceMonitorResult;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public interface NotificationDeliveryAgent {

    /**
     *
     * @param serviceMonitorResult
     */
    public void notify(ServiceMonitorResult serviceMonitorResult);

    /**
     *
     * @return
     */
    public SubscriptionDefinition getSubscriptionDefinition();

}
