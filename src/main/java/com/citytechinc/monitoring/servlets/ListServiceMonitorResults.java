package com.citytechinc.monitoring.servlets;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.domain.ServiceMonitorResult;
import com.citytechinc.monitoring.services.ServiceMonitorManager;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.util.List;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@SlingServlet(
        paths = Constants.LIST_SERVICE_MONITOR_RESULTS,
        methods = "GET")
public final class ListServiceMonitorResults extends AbstractJSONResponseServlet {

    public static final String SERVICE_MONITOR_CLASS_NAME = "serviceMonitorClassName";

    @Reference
    ServiceMonitorManager serviceMonitorManager;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

        final String serviceMonitorClassName = StringUtils.defaultString(request.getParameter(SERVICE_MONITOR_CLASS_NAME));

        final List<ServiceMonitorResult> serviceMonitorResults;

        if (StringUtils.isNotBlank(serviceMonitorClassName)) {
            serviceMonitorResults = serviceMonitorManager.getMonitorResults(serviceMonitorClassName);
        } else {
            serviceMonitorResults = serviceMonitorManager.getAllMonitorResults();
        }

        writeJsonResponse(response, serviceMonitorResults);
    }
}
