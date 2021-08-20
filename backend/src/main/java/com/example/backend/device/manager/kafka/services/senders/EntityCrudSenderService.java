package com.example.backend.device.manager.kafka.services.senders;

import com.example.backend.device.manager.kafka.producer.KafkaCrudEntityProducer;
import com.example.backend.device.manager.kafka.record.KafkaCrudRecordWrapper;
import com.example.backend.device.manager.kafka.record.OperationType;
import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;

public class EntityCrudSenderService<K, V extends KafkaRecordInterface<K>> extends BaseSenderService<K, KafkaCrudRecordWrapper<V>> {
    public EntityCrudSenderService(KafkaCrudEntityProducer<K, KafkaCrudRecordWrapper<V>> producer, String topic) {
        super(producer, topic);
    }

    public void postPersist(V object) {
        sendNewRecord(object, OperationType.CREATE);
    }

    public void postUpdates(List<V> objects) {
        for (V object : objects)
            postUpdate(object);
    }

    public void postUpdate(V object) {
        sendNewRecord(object, OperationType.UPDATE);
    }

    public void postRemoves(List<V> objects) {
        for (V object : objects)
            postRemove(object);
    }

    public void postRemove(V object) {
        sendNewRecord(object, OperationType.DELETE);
    }

    private void sendNewRecord(V object, OperationType operationType) {

        final ProducerRecord<K, KafkaCrudRecordWrapper<V>> record = new ProducerRecord<>(super.getTopic(), object.getId(),
                new KafkaCrudRecordWrapper<>(object, operationType));
        super.getProducer().produceKafkaRecord(record);
    }
}
