package com.citytechinc.monitoring.domain;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public final class ServiceMonitorResult extends ServiceMonitorResponse {

    private final Long processTimeInMilliseconds;
    private final Date callTime;
    private final String monitorClassName;

    public ServiceMonitorResult(ServiceMonitorResponseType serviceMonitorResponseType, Long processTimeInMilliseconds, Date callTime, String monitorClassName) {
        super(serviceMonitorResponseType, StringUtils.EMPTY);
        this.processTimeInMilliseconds = processTimeInMilliseconds;
        this.callTime = callTime;
        this.monitorClassName = monitorClassName;
    }

    public ServiceMonitorResult(ServiceMonitorResponseType serviceMonitorResponseType, String exceptionStackTrace, Long processTimeInMilliseconds, Date callTime, String monitorClassName) {
        super(serviceMonitorResponseType, exceptionStackTrace);
        this.processTimeInMilliseconds = processTimeInMilliseconds;
        this.callTime = callTime;
        this.monitorClassName = monitorClassName;
    }

    public Long getProcessTimeInMilliseconds() {
        return processTimeInMilliseconds;
    }

    public Date getCallTime() {
        return callTime;
    }

    public String getMonitorClassName() {
        return monitorClassName;
    }
}

