package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import com.example.backend.device.manager.kafka.record.OperationType;
import com.example.backend.device.manager.model.ControlSignal;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalEntityListener {
    @Autowired
    private KafkaEntitySender<ControlSignal> sender;

    private final String topic = "control-signals";

    @PostRemove
    public void postRemove(ControlSignal signal) {
        final ProducerRecord<Long, KafkaRecordWrapper<ControlSignal>> record = new ProducerRecord<>(topic, signal.getId(),
                new KafkaRecordWrapper<>(signal, OperationType.DELETE));
        sender.sendKafkaRecord(record);
    }

    @PostUpdate
    public void postUpdate(ControlSignal signal) {
        final ProducerRecord<Long, KafkaRecordWrapper<ControlSignal>> record = new ProducerRecord<>(topic, signal.getId(),
                new KafkaRecordWrapper<>(signal, OperationType.UPDATE));
        sender.sendKafkaRecord(record);
    }

    @PostPersist
    public void postPersist(ControlSignal signal) {
        final ProducerRecord<Long, KafkaRecordWrapper<ControlSignal>> record = new ProducerRecord<>(topic, signal.getId(),
                new KafkaRecordWrapper<>(signal, OperationType.CREATE));
        sender.sendKafkaRecord(record);
    }
}
