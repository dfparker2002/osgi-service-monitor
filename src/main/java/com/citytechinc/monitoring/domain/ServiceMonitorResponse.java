package com.citytechinc.monitoring.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public class ServiceMonitorResponse extends BaseDomain {

    private final ServiceMonitorResponseType serviceMonitorResponseType;
    private final String exceptionStackTrace;

    protected ServiceMonitorResponse(final ServiceMonitorResponseType serviceMonitorResponseType, final String exceptionStackTrace) {
        this.serviceMonitorResponseType = serviceMonitorResponseType;
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public static ServiceMonitorResponse SUCCESS() {
        return new ServiceMonitorResponse(ServiceMonitorResponseType.SUCCESS, StringUtils.EMPTY);
    }

    public static ServiceMonitorResponse SERVICE_UNAVAILABLE() {
        return new ServiceMonitorResponse(ServiceMonitorResponseType.SERVICE_UNAVAILABLE, StringUtils.EMPTY);
    }

    public static ServiceMonitorResponse UNEXPECTED_SERVICE_RESPONSE() {
        return new ServiceMonitorResponse(ServiceMonitorResponseType.UNEXPECTED_SERVICE_RESPONSE, StringUtils.EMPTY);
    }

    public static ServiceMonitorResponse EXCEPTION(final Exception exception) {
        return new ServiceMonitorResponse(ServiceMonitorResponseType.EXCEPTION, ExceptionUtils.getStackTrace(exception));
    }

    public ServiceMonitorResponseType getServiceMonitorResponseType() {
        return serviceMonitorResponseType;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

}
