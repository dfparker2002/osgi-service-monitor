package com.citytechinc.monitoring.servlets;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.services.ServiceMonitorManager;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@SlingServlet(
        paths = Constants.LIST_NOTIFICATION_DELIVERY_AGENTS,
        methods = "GET")
public final class ListNotificationDeliveryAgents extends AbstractJSONResponseServlet {

    @Reference
    ServiceMonitorManager serviceMonitorManager;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

        writeJsonResponse(response, serviceMonitorManager.listMonitors());
    }
}
