package com.example.backend.device.manager.kafka.producer;

import com.example.backend.device.manager.kafka.record.control.hub.KafkaHubControlRecordWrapper;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaHubControlProducer extends KafkaBaseEntityProducer<String, KafkaHubControlRecordWrapper> {
    public KafkaHubControlProducer(KafkaTemplate<String, KafkaHubControlRecordWrapper> template) {
        super(template);
    }
}
