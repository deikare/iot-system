package com.example.hubservice.management.hub.exceptions;

import com.example.hubservice.influxdb.mappers.InfluxHubStatusValue;
import com.example.hubservice.kafka.record.control.hub.HubControlType;

public class IllegalHubControlException extends RuntimeException {
    public IllegalHubControlException(InfluxHubStatusValue hubStatus, HubControlType hubControl) {
        super("Control " + hubControl + " has no impact on hubStatus " + hubStatus);
    }
}
