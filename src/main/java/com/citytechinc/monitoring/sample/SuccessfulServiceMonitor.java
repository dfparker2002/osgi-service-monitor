package com.citytechinc.monitoring.sample;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.domain.ServiceMonitorResponse;
import com.citytechinc.monitoring.domain.ServiceMonitorResponseType;
import com.citytechinc.monitoring.services.ServiceMonitor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(label = "Sample Always Successful Service Monitor", description = "")
@Service
@Properties({
        @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = Constants.CITYTECH_SERVICE_VENDOR_NAME) })
public final class SuccessfulServiceMonitor implements ServiceMonitor {

    @Override
    public ServiceMonitorResponse poll() {

        return new ServiceMonitorResponse(ServiceMonitorResponseType.SUCCESS);
    }
}
