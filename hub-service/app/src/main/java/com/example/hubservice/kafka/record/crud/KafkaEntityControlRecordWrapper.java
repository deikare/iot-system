package com.example.hubservice.kafka.record.crud;

public class KafkaEntityControlRecordWrapper<V> {
    private final V object;
    private final OperationType operationType;

    public KafkaEntityControlRecordWrapper(V object, OperationType operationType) {
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
