package com.citytechinc.monitoring.domain;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public final class ServiceMonitorRecordHolder extends BaseDomain {

    private AtomicBoolean isInAlarmState;
    private AtomicInteger numberOfConcurrentFailures;
    private final Queue<ServiceMonitorResult> results;

    public ServiceMonitorRecordHolder(final Integer queueSize) {

        isInAlarmState = new AtomicBoolean(false);
        numberOfConcurrentFailures = new AtomicInteger(0);
        results = new ArrayBlockingQueue<ServiceMonitorResult>(queueSize);
    }

    public Boolean isInAlarmState() {
        return isInAlarmState.get();
    }

    public void setAlarm() {
        isInAlarmState.set(true);
    }

    public void clearAlarmAndConcurrentFailures() {
        numberOfConcurrentFailures.set(0);
        isInAlarmState.set(false);
    }

    public Integer getNumberOfConcurrentFailures() {
        return numberOfConcurrentFailures.get();
    }

    public void addConcurrentFailure() {
        numberOfConcurrentFailures.getAndIncrement();
    }

    public void clearConcurrentFailures() {
        numberOfConcurrentFailures.set(0);
    }

    public Queue<ServiceMonitorResult> getResults() {
        return results;
    }
}
