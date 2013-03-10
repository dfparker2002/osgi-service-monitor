package com.citytechinc.monitoring.domain;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 *
 * @author CITYTECH, INC. 2013
 *
 */
public final class SubscriptionDefinition extends BaseDomain {

    private final Boolean isSubscribedToAll;
    private final List<String> subscribedMonitors;

    private SubscriptionDefinition(Boolean subscribedToAll, List<String> subscribedMonitors) {
        isSubscribedToAll = subscribedToAll;
        this.subscribedMonitors = subscribedMonitors;
    }

    public static SubscriptionDefinition ALL_MONITORS() {
        return new SubscriptionDefinition(true, null);
    }

    public static SubscriptionDefinition SPECIFIC_MONITORS(final List<String> subscribedMonitors) {

        if (subscribedMonitors != null) {
            return new SubscriptionDefinition(false, ImmutableList.copyOf(subscribedMonitors));
        } else {
            throw new IllegalArgumentException("You must supply a list of monitors");
        }
    }

    public Boolean getSubscribedToAll() {
        return isSubscribedToAll;
    }

    public List<String> getSubscribedMonitors() {
        return subscribedMonitors;
    }
}
