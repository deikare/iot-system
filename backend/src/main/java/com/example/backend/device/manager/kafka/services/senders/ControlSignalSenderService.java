package com.example.backend.device.manager.kafka.services.senders;

import com.example.backend.device.manager.kafka.producer.KafkaControlSignalProducer;
import com.example.backend.device.manager.kafka.producer.KafkaCrudEntityProducer;
import com.example.backend.device.manager.model.ControlSignal;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ControlSignalSenderService extends BaseSenderService<Long, ControlSignal> {


    public ControlSignalSenderService(KafkaControlSignalProducer producer, String topic) {
        super(producer, topic);
    }

    public void sendNewControlSignal(ControlSignal controlSignal) {
        final ProducerRecord<Long, ControlSignal> record = new ProducerRecord<>(super.getTopic(), controlSignal.getId(),
                controlSignal);
        super.getProducer().produceKafkaRecord(record);
    }
}
