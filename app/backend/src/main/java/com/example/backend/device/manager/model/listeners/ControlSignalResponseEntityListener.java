package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import com.example.backend.device.manager.kafka.record.OperationType;
import com.example.backend.device.manager.model.ControlSignalResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalResponseEntityListener {
    @Autowired
    private KafkaEntitySender<ControlSignalResponse> sender;

    private final String topic = "control-responses";

    @PostRemove
    public void postRemove(ControlSignalResponse response) {
        final ProducerRecord<Long, KafkaRecordWrapper<ControlSignalResponse>> record = new ProducerRecord<>(topic, response.getId(),
                new KafkaRecordWrapper<>(response, OperationType.DELETE));
        sender.sendKafkaRecord(record);
    }

    @PostUpdate
    public void postUpdate(ControlSignalResponse response) {
        final ProducerRecord<Long, KafkaRecordWrapper<ControlSignalResponse>> record = new ProducerRecord<>(topic, response.getId(),
                new KafkaRecordWrapper<>(response, OperationType.UPDATE));
        sender.sendKafkaRecord(record);
    }

    @PostPersist
    public void postPersist(ControlSignalResponse response) {
        final ProducerRecord<Long, KafkaRecordWrapper<ControlSignalResponse>> record = new ProducerRecord<>(topic, response.getId(),
                new KafkaRecordWrapper<>(response, OperationType.CREATE));
        sender.sendKafkaRecord(record);
    }
}
