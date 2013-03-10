package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.domain.ServiceMonitorResponse;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public interface ServiceMonitor {

    /**
     *
     * @return
     */
    public ServiceMonitorResponse poll();
}
