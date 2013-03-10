package com.citytechinc.monitoring.domain;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public class ServiceMonitorResponse extends BaseDomain {

    private final ServiceMonitorResponseType serviceMonitorResponseType;
    private final String exceptionStackTrace;

    public ServiceMonitorResponse(final ServiceMonitorResponseType serviceMonitorResponseType) {
        this.serviceMonitorResponseType = serviceMonitorResponseType;
        exceptionStackTrace = StringUtils.EMPTY;
    }

    public ServiceMonitorResponse(final String exceptionStackTrace) {
        serviceMonitorResponseType = ServiceMonitorResponseType.EXCEPTION;
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public ServiceMonitorResponseType getServiceMonitorResponseType() {
        return serviceMonitorResponseType;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

}
