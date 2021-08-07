package com.example.backend.device.manager.model.listeners.config;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubEntityListenerConfiguration {
    private final KafkaEntitySender<Long, Hub> sender;
    private final String topic = "hubs";

    public HubEntityListenerConfiguration(KafkaEntitySender<Long, Hub> sender) {
        this.sender = sender;
    }

    @Bean
    public EntityListenerImplementation<Long, Hub> hubListenerImplementation() {
        return new EntityListenerImplementation<>(sender, topic);
    }
}
