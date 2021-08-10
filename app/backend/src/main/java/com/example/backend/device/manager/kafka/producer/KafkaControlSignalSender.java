package com.example.backend.device.manager.kafka.producer;

import com.example.backend.device.manager.model.ControlSignal;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaControlSignalSender extends KafkaBaseEntitySender<Long, ControlSignal> {
    public KafkaControlSignalSender(KafkaTemplate<Long, ControlSignal> template) {
        super(template);
    }
}
