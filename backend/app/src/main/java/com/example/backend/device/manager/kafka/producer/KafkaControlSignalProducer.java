package com.example.backend.device.manager.kafka.producer;

import com.example.backend.device.manager.model.ControlSignal;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaControlSignalProducer extends KafkaBaseEntityProducer<Long, ControlSignal> {
    public KafkaControlSignalProducer(KafkaTemplate<Long, ControlSignal> template) {
        super(template);
    }
}
