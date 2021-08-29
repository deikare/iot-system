package com.example.hubservice.kafka.record.control.hub;

import com.example.hubservice.management.hub.model.Hub;

public class KafkaHubControlRecordWrapper {
    private Hub hub;
    private HubControlType controlType;

    public KafkaHubControlRecordWrapper() {
    }

    public KafkaHubControlRecordWrapper(Hub hub, HubControlType controlType) {
        this.hub = hub;
        this.controlType = controlType;
    }

    public Hub getHub() {
        return hub;
    }

    public HubControlType getControlType() {
        return controlType;
    }

    @Override
    public String toString() {
        return "KafkaHubControlTypeRecordWrapper{" +
                "hub=" + hub +
                ", controlType=" + controlType +
                '}';
    }
}
