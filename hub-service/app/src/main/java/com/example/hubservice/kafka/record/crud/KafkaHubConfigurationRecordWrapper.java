package com.example.hubservice.kafka.record.crud;

import com.example.hubservice.management.hub.model.Hub;

public class KafkaHubConfigurationRecordWrapper {
    private Hub object;
    private OperationType operationType;

    public KafkaHubConfigurationRecordWrapper() {
    }

    public KafkaHubConfigurationRecordWrapper(Hub object, OperationType operationType) {
        this.object = object;
        this.operationType = operationType;
    }

    public Hub getObject() {
        return object;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        return "KafkaRecordWrapper{" +
                "object=" + object +
                ", operationType=" + operationType +
                '}';
    }
}
