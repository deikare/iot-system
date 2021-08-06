package com.example.backend.device.manager.kafka.record;

public class KafkaRecordWrapper<B> {
    private final B object;
    private final OperationType operationType;

    public KafkaRecordWrapper(B object, OperationType operationType) {
        this.object = object;
        this.operationType = operationType;
    }

    public B getObject() {
        return object;
    }

    public OperationType getOperationType() {
        return operationType;
    }
}
