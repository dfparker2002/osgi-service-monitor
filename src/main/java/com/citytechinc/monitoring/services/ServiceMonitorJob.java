package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.constants.Constants;
import com.google.common.collect.Maps;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.OsgiUtil;
import org.apache.sling.commons.scheduler.Job;
import org.apache.sling.commons.scheduler.JobContext;
import org.apache.sling.commons.scheduler.Scheduler;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(label = "Service Monitor Job", description = "", metatype = true)
@Service
@Properties({
    @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = Constants.CITYTECH_SERVICE_VENDOR_NAME) })
public final class ServiceMonitorJob implements Job {

    private static final boolean DEFAULT_IS_ENABLED = false;
    @Property(label = "Enabled", boolValue = DEFAULT_IS_ENABLED, description = "Is enabled?")
    private static final String IS_ENABLED_PROPERTY = "isEnabled";
    
    @Property(label = "Schedule", value = Constants.SERVICE_MONITOR_POLL_JOB_DEFAULT_JOB_SCHEDULE, description = "Uses scheduling expressions accepted by org.apache.sling.commons.scheduler.Scheduler; see http://www.docjar.com/docs/api/org/quartz/CronExpression.html for explanation, and http://www.docjar.com/docs/api/org/quartz/CronTrigger.html for examples.")
    private static final String SCHEDULE_PROPERTY = "schedule";

    @Reference
    Scheduler scheduler;

    @Reference
    ServiceMonitorManager serviceMonitorManager;

    @Activate
    protected void activate(final Map<String, Object> properties) throws Exception {

        if (OsgiUtil.toBoolean(properties.get(IS_ENABLED_PROPERTY), DEFAULT_IS_ENABLED)) {

            final Map<String, Serializable> config = Maps.newHashMap();
            scheduler.addJob(Constants.SERVICE_MONITOR_POLL_JOB_KEY, this, config, OsgiUtil.toString(properties.get(SCHEDULE_PROPERTY), Constants.SERVICE_MONITOR_POLL_JOB_DEFAULT_JOB_SCHEDULE), false);
        }
    }

    @Deactivate
    protected void deactivate(final Map<String, Object> properties) throws Exception {

        scheduler.removeJob(Constants.SERVICE_MONITOR_POLL_JOB_KEY);
    }

    @Override
    public void execute(final JobContext context) {

        serviceMonitorManager.poll();
    }
}
