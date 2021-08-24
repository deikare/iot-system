package com.example.backend.device.manager.kafka.services.senders;

import com.example.backend.device.manager.kafka.producer.KafkaBaseEntityProducer;

public class BaseSenderService<K, V>  {
    private final KafkaBaseEntityProducer<K, V> producer;
    private final String topic;


    public BaseSenderService(KafkaBaseEntityProducer<K, V> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    public KafkaBaseEntityProducer<K, V> getProducer() {
        return producer;
    }

    public String getTopic() {
        return topic;
    }
}
