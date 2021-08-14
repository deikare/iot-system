package com.example.backend.device.manager.model.listeners.generic.implementations;

import com.example.backend.device.manager.kafka.producer.KafkaCrudEntitySender;
import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import com.example.backend.device.manager.kafka.record.OperationType;
import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.listeners.generic.interfaces.EntityListenerInterface;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class EntityListenerImplementation <K, V extends KafkaRecordInterface<K>> implements EntityListenerInterface<V> {
    private final KafkaCrudEntitySender<K, V> sender;
    private final String topic;

    public EntityListenerImplementation(KafkaCrudEntitySender<K, V> sender, String topic) {
        this.sender = sender;
        this.topic = topic;
    }

    @Override
    @PostPersist
    public void postPersist(V object) {
        sendNewRecord(object, OperationType.CREATE);
    }

    @Override
    @PostUpdate
    public void postUpdate(V object) {
        sendNewRecord(object, OperationType.UPDATE);
    }

    @Override
    @PostRemove
    public void postRemove(V object) {
        sendNewRecord(object, OperationType.DELETE);
    }

    private void sendNewRecord(V object, OperationType operationType) {
        final ProducerRecord<K, KafkaRecordWrapper<V>> record = new ProducerRecord<>(topic, object.getId(),
                new KafkaRecordWrapper<>(object, operationType));
        sender.sendKafkaRecord(record);
    }
}
