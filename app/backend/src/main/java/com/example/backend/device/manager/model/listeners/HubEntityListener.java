package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import com.example.backend.device.manager.kafka.record.OperationType;
import com.example.backend.device.manager.model.Hub;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class HubEntityListener {
    @Autowired
    private KafkaEntitySender<Hub> sender;

    private final String topic = "hubs";

    @PostRemove
    public void postRemove(Hub hub) {
        final ProducerRecord<Long, KafkaRecordWrapper<Hub>> record = new ProducerRecord<>(topic, hub.getId(),
                new KafkaRecordWrapper<>(hub, OperationType.DELETE));
        sender.sendKafkaRecord(record);
    }

    @PostUpdate
    public void postUpdate(Hub hub) {
        final ProducerRecord<Long, KafkaRecordWrapper<Hub>> record = new ProducerRecord<>(topic, hub.getId(),
                new KafkaRecordWrapper<>(hub, OperationType.UPDATE));
        sender.sendKafkaRecord(record);
    }

    @PostPersist
    public void postPersist(Hub hub) {
        final ProducerRecord<Long, KafkaRecordWrapper<Hub>> record = new ProducerRecord<>(topic, hub.getId(),
                new KafkaRecordWrapper<>(hub, OperationType.CREATE));
        sender.sendKafkaRecord(record);
    }
}
