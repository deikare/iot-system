package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import com.example.backend.device.manager.kafka.record.OperationType;
import com.example.backend.device.manager.model.Device;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DeviceEntityListener {
    @Autowired
    private KafkaEntitySender<Device> sender;

    private final String topic = "devices";

    @PostRemove
    public void postRemove(Device device) {
        final ProducerRecord<Long, KafkaRecordWrapper<Device>> record = new ProducerRecord<>(topic, device.getId(),
                new KafkaRecordWrapper<>(device, OperationType.DELETE));
        sender.sendKafkaRecord(record);
    }

    @PostUpdate
    public void postUpdate(Device device) {
        final ProducerRecord<Long, KafkaRecordWrapper<Device>> record = new ProducerRecord<>(topic, device.getId(),
                new KafkaRecordWrapper<>(device, OperationType.UPDATE));
        sender.sendKafkaRecord(record);
    }

    @PostPersist
    public void postPersist(Device device) {
        final ProducerRecord<Long, KafkaRecordWrapper<Device>> record = new ProducerRecord<>(topic, device.getId(),
                new KafkaRecordWrapper<>(device, OperationType.CREATE));
        sender.sendKafkaRecord(record);
    }

}
