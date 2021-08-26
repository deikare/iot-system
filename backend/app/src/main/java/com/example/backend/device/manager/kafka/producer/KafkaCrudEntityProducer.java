package com.example.backend.device.manager.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;

public class KafkaCrudEntityProducer<K, V> extends KafkaBaseEntityProducer<K, V> {
    public KafkaCrudEntityProducer(KafkaTemplate<K, V> template) {
        super(template);
    }
}
