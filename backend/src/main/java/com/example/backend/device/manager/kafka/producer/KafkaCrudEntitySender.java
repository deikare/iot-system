package com.example.backend.device.manager.kafka.producer;

import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaCrudEntitySender<K, V> extends KafkaBaseEntitySender<K, KafkaRecordWrapper<V>> {
    public KafkaCrudEntitySender(KafkaTemplate<K, KafkaRecordWrapper<V>> template) {
        super(template);
    }
}
