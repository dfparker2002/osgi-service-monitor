package com.citytechinc.monitoring.constants;

/**
 * 
 * @author CITYTECH, INC. 2013
 * 
 */
public final class Constants {

    /**
     * Deny outside instantiation.
     */
    private Constants() {

    }

    public static final String CITYTECH_SERVICE_VENDOR_NAME = "CITYTECH, INC.";

    public static final String SERVICE_MONITOR_POLL_JOB_KEY = "ct-srvmonjob";
    public static final String SERVICE_MONITOR_POLL_JOB_DEFAULT_JOB_SCHEDULE = "0 0 * * * ?";

    public static final String SERVICE_MONITOR_MANAGER_DEFAULT_SEQUENTIAL_FAILURES_BEFORE_NOTIFICATION = "3";
    public static final Integer SERVICE_MONITOR_MANAGER_DEFAULT_QUEUE_SIZE = 50;
    public static final Integer SERVICE_MONITOR_MANAGER_DEFAULT_THREAD_POOL_SIZE = 10;

    public static final String ABSTRACT_JSON_RESPONSE_SERVLET_CONTENT_TYPE = "application/json";
    public static final String ABSTRACT_JSON_RESPONSE_SERVLET_CHARACTER_ENCODING = "utf8";
    public static final String ABSTRACT_JSON_RESPONSE_SERVLET_DEFAULT_DATE_FORMAT = "MM/dd/yyyy hh:mm aaa z";

    private static final String SERVLET_ROOT_PATH = "/bin/citytechinc/monitoring";
    public static final String LIST_AVAILABLE_SERVICE_MONITORS = SERVLET_ROOT_PATH + "/listAvailableServiceMonitors";
    public static final String LIST_SERVICE_MONITOR_RESULTS = SERVLET_ROOT_PATH + "/listServiceMonitorResults";

}
