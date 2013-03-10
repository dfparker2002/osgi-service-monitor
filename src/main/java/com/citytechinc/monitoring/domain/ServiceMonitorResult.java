package com.citytechinc.monitoring.domain;

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

    public ServiceMonitorResult(final ServiceMonitorResponseType serviceMonitorResponseType,
                                final Long processTimeInMilliseconds,
                                final Date callTime,
                                final String monitorClassName) {

        super(serviceMonitorResponseType);
        this.processTimeInMilliseconds = processTimeInMilliseconds;
        this.callTime = callTime;
        this.monitorClassName = monitorClassName;
    }

    public ServiceMonitorResult(final String exceptionStackTrace,
                                final Long processTimeInMilliseconds,
                                final Date callTime,
                                final String monitorClassName) {

        super(exceptionStackTrace);
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

