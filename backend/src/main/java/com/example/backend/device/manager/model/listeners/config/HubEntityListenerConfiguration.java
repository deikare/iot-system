package com.example.backend.device.manager.model.listeners.config;

import com.example.backend.device.manager.kafka.producer.KafkaCrudEntitySender;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class HubEntityListenerConfiguration {
    private final KafkaCrudEntitySender<String, Hub> sender;
    private final String topic = "hubs";

    public HubEntityListenerConfiguration(KafkaCrudEntitySender<String, Hub> sender) {
        this.sender = sender;
    }

    @Bean
    public EntityListenerImplementation<String, Hub> hubListenerImplementation() {
        return new EntityListenerImplementation<>(sender, topic);
    }
}
