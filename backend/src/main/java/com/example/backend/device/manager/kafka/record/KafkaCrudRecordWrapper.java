package com.example.backend.device.manager.kafka.record;

public class KafkaCrudRecordWrapper<V> {
    private final V object;
    private final OperationType operationType;

    public KafkaCrudRecordWrapper(V object, OperationType operationType) {
        this.object = object;
        this.operationType = operationType;
    }

    public V getObject() {
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
