# OSGi Service Monitor

[CITYTECH, Inc.](http://www.citytechinc.com)

## Overview

The OSGi Service Monitor is a bundle aimed at aiding engineers to keep track of the (likely external) services they depend on. The OSGi Service Monitor provides:

1. A service signature for monitors
2. A service signature for delivery agents
3. A scheduled job for across-the-board polling
4. A central service tasked with keeping track of monitors and delivery agents as they come and go, in addition to poll responses.
5. A set of servlets rendering data as JSON as well as a JMX bean 

## Usage

Simply implement the com.citytechinc.monitoring.services.ServiceMonitor interface for monitors and the com.citytechinc.monitoring.services.NotificationDeliveryAgent interface for delivery agents. The ServiceMonitorManager automatically tracks the monitors and delivery agents as they come on and offline. A NotificationDeliveryAgent can choose to receive unsuccessful event notifications for all registered ServiceMonitors or a list of defined monitors.

The ServiceMonitorManager keeps track of results from the monitors as it stores:

1. The time the ServiceMonitor began its poll
2. The process time for the ServiceMonitor in milliseconds
3. The exception stack trace if an exception occurred

... per ServiceMonitor.

Tracked per ServiceMonitor, after the max sequential unsuccesful polls is reached, the relevant DeliveryAgents are notified. Polling for ServiceMonitors which are "in a state of alarm" is suspended until the alarm has been cleared.

Presently, the only way to clear the alarm is to invoke resetAllAlarms() or resetAlarm(String monitorName) found on the ServiceMonitorManagerMBean.

For extensibility/expansion purposes, the data received by the ServiceMonitorManager is also available, as JSON, via calls to a few servlets:

1. /bin/citytechinc/monitoring/listNotificationDeliveryAgents
2. /bin/citytechinc/monitoring/listServiceMonitorResults
3. /bin/citytechinc/monitoring/listServiceMonitors

## Installation

...

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
