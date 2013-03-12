package com.citytechinc.monitoring.sample;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.domain.ServiceMonitorResponse;
import com.citytechinc.monitoring.domain.ServiceMonitorResponseType;
import com.citytechinc.monitoring.services.ServiceMonitor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * This monitor sometimes returns SUCCESS, sometimes returns UNEXPECTED_SERVICE_RESPONSE.
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(label = "Sample Sometimes Successful Service Monitor", description = "")
@Service
@Properties({
        @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = Constants.CITYTECH_SERVICE_VENDOR_NAME) })
public final class SometimesSuccessfulServiceMonitor implements ServiceMonitor {

    private static final Random RANDOM = new SecureRandom();

    @Override
    public ServiceMonitorResponse poll() {

        final ServiceMonitorResponse response;

        if (RANDOM.nextBoolean()) {
            response = new ServiceMonitorResponse(ServiceMonitorResponseType.SUCCESS);
        } else {
            response = new ServiceMonitorResponse(ServiceMonitorResponseType.UNEXPECTED_SERVICE_RESPONSE);
        }

        return response;
    }
}
