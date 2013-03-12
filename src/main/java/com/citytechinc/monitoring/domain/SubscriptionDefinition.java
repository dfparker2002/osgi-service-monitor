package com.citytechinc.monitoring.domain;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public final class SubscriptionDefinition extends BaseDomain {

    private final SubscriptionType subscriptionType;
    private final List<String> monitors;

    private SubscriptionDefinition(SubscriptionType subscriptionType, List<String> monitors) {
        this.subscriptionType = subscriptionType;
        this.monitors = monitors;
    }

    public static SubscriptionDefinition SUBSCRIBE_TO_ALL_MONITORS() {
        return new SubscriptionDefinition(SubscriptionType.SUBSCRIBED_TO_ALL, null);
    }

    public static SubscriptionDefinition SUBSCRIBE_TO_SPECIFIC_MONITORS(final List<String> subscribedMonitors) {

        if (subscribedMonitors != null) {
            return new SubscriptionDefinition(SubscriptionType.SUBSCRIBED_TO_SPECIFIC, ImmutableList.copyOf(subscribedMonitors));
        } else {
            throw new IllegalArgumentException("You must supply a list of monitors");
        }
    }

    public static SubscriptionDefinition UNSUBSCRIBE_FROM_SPECIFIC_MONITORS(final List<String> unSubscribedMonitors) {

        if (unSubscribedMonitors != null) {
            return new SubscriptionDefinition(SubscriptionType.UNSUBSCRIBED_FROM_SPECIFIC, ImmutableList.copyOf(unSubscribedMonitors));
        } else {
            throw new IllegalArgumentException("You must supply a list of monitors");
        }
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public List<String> getMonitors() {
        return monitors;
    }

    public enum SubscriptionType {

        SUBSCRIBED_TO_ALL,
        SUBSCRIBED_TO_SPECIFIC,
        UNSUBSCRIBED_FROM_SPECIFIC;
    }
}
