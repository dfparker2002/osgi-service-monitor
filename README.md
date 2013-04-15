# Felix OSGi Service Monitor

[CITYTECH, Inc.](http://www.citytechinc.com)

## Introduction

The Felix OSGi Service Monitor is a bundle aimed at aiding engineers in the tracking of services they depend on. It was designed for use in an Adobe CQ project to monitor and alert on external service dependencies such as critical data feeds that are sourced outside CQ (Apache Felix OSGi). The OSGi Service Monitor provides:

1. A service signature for monitors -- responsible for polling
2. A service signature for delivery agents -- responsible for responding to sequential, unsuccessful poll attempts
3. A scheduled job for across-the-board polling
4. A central service tasked with keeping track of monitors and delivery agents as they come and go, in addition to poll responses
5. A set of servlets rendering data as JSON as well as a JMX bean

## Sample Business Use Case

An engineer develops an OSGi service that is critical to the operation of a site. This provides a simple mechanism to monitor and alert on these services.

For example, an OSGi [WeatherService](https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/weatherservice/WeatherService.java) pulls forecasting and other weather data from an external system. If the [WeatherService](https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/weatherservice/WeatherService.java) goes offline, receives an unexpected response, or no response at all from the external system, the [WeatherServiceMonitor](https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/weatherservice/WeatherServiceMonitor.java) would indicate failures and the product owner could receive an email/text/alert/etc... The details of how the product owner is notified are configurable by the developed instances of the notification delivery agents.

## Technical Overview

The ServiceMonitorManager automatically tracks the monitors and delivery agents as they come on and offline. A NotificationDeliveryAgent can choose to receive unsuccessful event notifications for all registered ServiceMonitors or a list of defined monitors.

Registration of (0-to-many) `ServiceMonitor` is automatic, requring a simple implementation of:

```
package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.domain.ServiceMonitorResponse;

public interface ServiceMonitor {

    public ServiceMonitorResponse poll();
}
```

Registration of (0-to-many) `NotificationDeliveryAgent` is also automatic, requiring an implementation of:

```
package com.citytechinc.monitoring.services;

import com.citytechinc.monitoring.domain.SubscriptionDefinition;
import com.citytechinc.monitoring.domain.ServiceMonitorResult;

public interface NotificationDeliveryAgent {

    public void notify(ServiceMonitorResult serviceMonitorResult);
    public SubscriptionDefinition getSubscriptionDefinition();
}
```
Tracked per `ServiceMonitor`, the `ServiceMonitorManager` stores `ServiceMonitorResponses` from the monitors it polls. Most importantly, the manager stores details such as:

1. The time the ServiceMonitor began its poll
2. The process time for the ServiceMonitor in milliseconds
3. The exception stack trace if an exception occurred

After the max sequential unsuccesful polls is reached for a given `ServiceMonitor`, the relevant `NotificationDeliveryAgent`s are notified. Polling for `ServiceMonitor` which are "in a state of alarm" is suspended until the alarm has been cleared.

Presently, to clear an alarm an engineer must invoke the `ServiceMonitorManager.resetAllAlarms()` or `ServiceMonitorManager.resetAlarm(String monitorName)`. For ease, these signatures are also available on the provided JMX MBean. The MBean also provides a quick birdseye view of the:

1. List of monitors
2. List of delivery agents
3. List of alarmed monitors

For extensibility/expansion purposes, the data received by the ServiceMonitorManager is also available, as JSON, via GET calls to a few servlets:

1. `/bin/citytechinc/monitoring/listNotificationDeliveryAgents`
2. `/bin/citytechinc/monitoring/listServiceMonitorResults`
3. `/bin/citytechinc/monitoring/listServiceMonitors`

## Sample Usage

There are three provided sample services - one `NotificationDeliveryAgent` and three `ServiceMonitor`.

The [SampleNotificationDeliveryAgent] [1] simply logs when it is invoked.

There are two basic `ServiceMonitor` implementations, [SuccessfulServiceMonitor] [3] and [SometimesSuccessfulServiceMonitor] [2], return successful poll responses and random success/unexpected responses, respectively. The third sample implementation [WeatherServiceMonitor] [4] is the most in-depth and examines how you might keep tabs on other services.

  [1]: https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/SampleNotificationDeliveryAgent.java          "SampleNotificationDeliveryAgent.java"
  [2]: https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/SometimesSuccessfulServiceMonitor.java        "SometimesSuccessfulServiceMonitor.java"
  [3]: https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/SuccessfulServiceMonitor.java                 "SuccessfulServiceMonitor.java"
  [4]: https://github.com/Citytechinc/osgi-service-monitor/blob/master/src/main/java/com/citytechinc/monitoring/sample/weatherservice/WeatherServiceMonitor.java                 "WeatherServiceMonitor.java"

## Installation

... todo ...

## Todo

1. Develop UI for ServiceMonitor results. Presently these are only availble as JSON via a GET call to `/bin/citytechinc/monitoring/listServiceMonitorResults`

## Notes

Please contact [Josh Durbin](mailto:jdurbin@citytechinc.com) with any questions.

## Versioning

Follows [Semantic Versioning](http://semver.org/) guidelines.

## License

Copyright 2013 CITYTECH, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
