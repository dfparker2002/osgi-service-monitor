package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.constants.Constants;
import com.citytechinc.monitoring.domain.ServiceMonitorRecordHolder;
import com.citytechinc.monitoring.domain.ServiceMonitorResponse;
import com.citytechinc.monitoring.domain.ServiceMonitorResponseType;
import com.citytechinc.monitoring.domain.ServiceMonitorResult;
import com.citytechinc.monitoring.domain.SubscriptionDefinition;
import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.OsgiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
@Component(label = "Service Monitor Manager", description = "Responsible for maintaining references to Service Monitors and Delivery Agents, polling the monitors, maintaining records for the monitors, and triggering the delivery agents when a failure has occurred.", metatype = true)
@Service()
@Properties(value = {
        @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = Constants.CITYTECH_SERVICE_VENDOR_NAME) })
public final class ServiceMonitorManagerImpl implements ServiceMonitorManager {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMonitorManagerImpl.class);

    private ListeningExecutorService monitorExecutorService = null;
    private ExecutorService notificationExecutorService = null;

    @Property(label = "Service Monitor Executor Pool Size", intValue = Constants.SERVICE_MONITOR_MANAGER_DEFAULT_THREAD_POOL_SIZE, description = "The size of the thread pool used to execute service monitor poll requests.")
    private static final String SERVICE_MONITOR_EXECUTOR_POOL_SIZE = "serviceMonitorExecutorPoolSize";
    private Integer serviceMonitorExecutorPoolSize;

    @Property(label = "Notification Executor Pool Size", intValue = Constants.SERVICE_MONITOR_MANAGER_DEFAULT_THREAD_POOL_SIZE, description = "The size of the thread pool used to execute notifications for alarmed monitors.")
    private static final String NOTIFICATION_EXECUTOR_POOL_SIZE = "notificationExecutorPoolSize";
    private Integer notificationExecutorPoolSize;

    @Property(label = "Sequential Failure Limit", intValue = Constants.SERVICE_MONITOR_MANAGER_DEFAULT_SEQUENTIAL_FAILURES_BEFORE_NOTIFICATION, description = "The number of sequential failures, tracked by individually by each ServiceMonitor, before a notification is triggered.")
    private static final String SEQUENTIAL_FAILURES_BEFORE_NOTIFICATION_PROPERTY = "sequentialFailuresBeforeNotification";
    private Integer sequentialFailuresBeforeNotification;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, referenceInterface = NotificationDeliveryAgent.class, bind = "bindNotificationDeliveryAgent", unbind = "unbindNotificationDeliveryAgent")
    private Map<NotificationDeliveryAgent, SubscriptionDefinition> notificationDeliveryAgents = Maps.newConcurrentMap();

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, referenceInterface = ServiceMonitor.class, bind = "bindServiceMonitor", unbind = "unbindServiceMonitor")
    private Map<ServiceMonitor, ServiceMonitorRecordHolder> serviceMonitors = Maps.newConcurrentMap();

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) throws Exception {

        serviceMonitorExecutorPoolSize = OsgiUtil.toInteger(properties.get(SERVICE_MONITOR_EXECUTOR_POOL_SIZE), Constants.SERVICE_MONITOR_MANAGER_DEFAULT_THREAD_POOL_SIZE);
        notificationExecutorPoolSize = OsgiUtil.toInteger(properties.get(NOTIFICATION_EXECUTOR_POOL_SIZE), Constants.SERVICE_MONITOR_MANAGER_DEFAULT_THREAD_POOL_SIZE);
        sequentialFailuresBeforeNotification = OsgiUtil.toInteger(properties.get(SEQUENTIAL_FAILURES_BEFORE_NOTIFICATION_PROPERTY), Constants.SERVICE_MONITOR_MANAGER_DEFAULT_SEQUENTIAL_FAILURES_BEFORE_NOTIFICATION);

        if (monitorExecutorService != null) {
            monitorExecutorService.shutdownNow();
        }
        monitorExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(serviceMonitorExecutorPoolSize));

        if (notificationExecutorService != null) {
            notificationExecutorService.shutdownNow();
        }
        notificationExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(notificationExecutorPoolSize));
    }

    @Deactivate
    protected void deactivate(final Map<String, Object> properties) throws Exception {
        monitorExecutorService.shutdownNow();
        notificationExecutorService.shutdownNow();
    }

    protected void bindServiceMonitor(final ServiceMonitor serviceMonitor) {
        serviceMonitors.put(serviceMonitor, new ServiceMonitorRecordHolder(Constants.SERVICE_MONITOR_MANAGER_DEFAULT_QUEUE_SIZE));
    }

    protected void unbindServiceMonitor(final ServiceMonitor serviceMonitor) {
        serviceMonitors.remove(serviceMonitor);
    }

    protected void bindNotificationDeliveryAgent(final NotificationDeliveryAgent notificationDeliveryAgent) {
        notificationDeliveryAgents.put(notificationDeliveryAgent, notificationDeliveryAgent.getSubscriptionDefinition());
    }

    protected void unbindNotificationDeliveryAgent(final NotificationDeliveryAgent notificationDeliveryAgent) {
        notificationDeliveryAgents.remove(notificationDeliveryAgent);
    }

    @Override
    public void poll() {

        for (final ServiceMonitor serviceMonitor : serviceMonitors.keySet()) {

            final ServiceMonitorRecordHolder recordHolder = serviceMonitors.get(serviceMonitor);
            final String serviceMonitorClassName = serviceMonitor.getClass().getName();

            if (recordHolder.getResults().isEmpty() || !recordHolder.isInAlarmState()) {

                final ListenableFuture<ServiceMonitorResult> pollResponseFuture = monitorExecutorService.submit(new Callable<ServiceMonitorResult>() {

                    public ServiceMonitorResult call() {

                        final Date callTime = new Date();

                        final Stopwatch stopwatch = new Stopwatch();
                        stopwatch.start();

                        LOG.debug("Calling monitorExecutorService monitor '{}' at '{}'", serviceMonitorClassName, callTime);
                        final ServiceMonitorResponse serviceMonitorResponse = serviceMonitor.poll();

                        stopwatch.stop();

                        final ServiceMonitorResult serviceMonitorResult;

                        if (serviceMonitorResponse.getServiceMonitorResponseType() == ServiceMonitorResponseType.EXCEPTION) {

                            serviceMonitorResult = new ServiceMonitorResult(serviceMonitorResponse.getServiceMonitorResponseType(),
                                    serviceMonitorResponse.getExceptionStackTrace(),
                                    stopwatch.elapsed(TimeUnit.MILLISECONDS),
                                    callTime,
                                    serviceMonitorClassName);
                        } else {

                            serviceMonitorResult = new ServiceMonitorResult(serviceMonitorResponse.getServiceMonitorResponseType(),
                                    stopwatch.elapsed(TimeUnit.MILLISECONDS),
                                    callTime,
                                    serviceMonitorClassName);
                        }

                        return serviceMonitorResult;
                    }
                });

                Futures.addCallback(pollResponseFuture, new FutureCallback<ServiceMonitorResult>() {

                    public void onSuccess(final ServiceMonitorResult pollResult) {

                        LOG.debug("Handling {} response for monitor '{}' in {} ms at '{}'", new Object[] {pollResult.getServiceMonitorResponseType(), pollResult.getMonitorClassName(), pollResult.getProcessTimeInMilliseconds(), pollResult.getCallTime()});

                        recordHolder.getResults().offer(pollResult);

                        if (pollResult.getServiceMonitorResponseType() != ServiceMonitorResponseType.SUCCESS) {

                            recordHolder.addConcurrentFailure();

                            if (recordHolder.getNumberOfConcurrentFailures() >= sequentialFailuresBeforeNotification) {

                                for (final NotificationDeliveryAgent notificationDeliveryAgent : notificationDeliveryAgents.keySet()) {

                                    final SubscriptionDefinition subscriptionDefinition = notificationDeliveryAgents.get(notificationDeliveryAgent);

                                    if (subscriptionDefinition.getSubscriptionType() == SubscriptionDefinition.SubscriptionType.SUBSCRIBED_TO_ALL
                                            || ((subscriptionDefinition.getSubscriptionType() == SubscriptionDefinition.SubscriptionType.SUBSCRIBED_TO_SPECIFIC)
                                                && (subscriptionDefinition.getMonitors().contains(pollResult.getMonitorClassName())))
                                            || ((subscriptionDefinition.getSubscriptionType() == SubscriptionDefinition.SubscriptionType.UNSUBSCRIBED_FROM_SPECIFIC)
                                                && (!subscriptionDefinition.getMonitors().contains(pollResult.getMonitorClassName())))) {

                                        notificationExecutorService.submit(new Callable<Void>() {

                                            public Void call() {

                                                LOG.debug("Calling NotificationDeliveryAgent '{}' for monitor '{}'", notificationDeliveryAgent.getClass().getName(), pollResult.getMonitorClassName());
                                                notificationDeliveryAgent.notify(pollResult);

                                                return null;
                                            }
                                        });
                                    }
                                }

                                recordHolder.setAlarm();
                            }

                        } else {

                            recordHolder.clearConcurrentFailures();
                        }
                    }

                    public void onFailure(final Throwable thrown) {

                        LOG.error("Handling failure for monitor '{}'.", serviceMonitorClassName, thrown);
                    }
                });

            } else {

                LOG.debug("Monitor '{}' already in alarm state, skipping", serviceMonitorClassName);
            }
        }
    }

    @Override
    public List<String> listMonitors() {
        return Lists.transform(Lists.newArrayList(serviceMonitors.keySet()), REDUCE_LIST_OF_OBJECTS_TO_LIST_OF_CLASS_NAMES);
    }

    @Override
    public List<String> listNotificationDeliveryAgents() {
        return Lists.transform(Lists.newArrayList(notificationDeliveryAgents.keySet()), REDUCE_LIST_OF_OBJECTS_TO_LIST_OF_CLASS_NAMES);
    }

    @Override
    public List<ServiceMonitorResult> getAllMonitorResults() {

        final ImmutableList.Builder<ServiceMonitorResult> monitorResultBuilder = new ImmutableList.Builder<ServiceMonitorResult>();

        for (final ServiceMonitorRecordHolder serviceMonitorRecordHolder : serviceMonitors.values()) {
            monitorResultBuilder.addAll(serviceMonitorRecordHolder.getResults());
        }

        return monitorResultBuilder.build();
    }

    @Override
    public List<ServiceMonitorResult> getMonitorResults(final String monitorName) {

        List<ServiceMonitorResult> results = Lists.newArrayList();

        for (final ServiceMonitor serviceMonitor : serviceMonitors.keySet()) {

            if (StringUtils.equalsIgnoreCase(serviceMonitor.getClass().getName(), monitorName)) {

                results = Lists.newArrayList(serviceMonitors.get(serviceMonitor).getResults().iterator());
            }
        }

        return results;
    }

    @Override
    public List<String> getAlarmedMonitors() {

        final ImmutableList.Builder<String> alarmedMonitorsBuilder = new ImmutableList.Builder<String>();

        for (final ServiceMonitor serviceMonitor : serviceMonitors.keySet()) {

            final ServiceMonitorRecordHolder recordHolder = serviceMonitors.get(serviceMonitor);

            if (recordHolder.isInAlarmState()) {
                alarmedMonitorsBuilder.add(serviceMonitor.getClass().getName());
            }
        }

        return alarmedMonitorsBuilder.build();
    }

    @Override
    public void resetAllAlarms() {

        for (final ServiceMonitorRecordHolder recordHolder : serviceMonitors.values()) {

            recordHolder.clearAlarmAndConcurrentFailures();
        }
    }

    @Override
    public void resetAlarm(final String monitorName) {

        for (final ServiceMonitor serviceMonitor : serviceMonitors.keySet()) {

            if (StringUtils.equalsIgnoreCase(serviceMonitor.getClass().getName(), monitorName)) {

                serviceMonitors.get(serviceMonitor).clearAlarmAndConcurrentFailures();
            }
        }
    }

    private static final Function<Object, String> REDUCE_LIST_OF_OBJECTS_TO_LIST_OF_CLASS_NAMES = new Function<Object, String>() {

        @Override
        public String apply(final Object object) {
            return object.getClass().getName();
        }
    };
}
