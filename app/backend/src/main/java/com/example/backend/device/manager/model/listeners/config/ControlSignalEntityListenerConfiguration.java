package com.example.backend.device.manager.model.listeners.config;

import com.example.backend.device.manager.kafka.producer.KafkaCrudEntitySender;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalEntityListenerConfiguration {
    private final KafkaCrudEntitySender<Long, ControlSignal> sender;
    private final String topic = "control-signals";

    public ControlSignalEntityListenerConfiguration(KafkaCrudEntitySender<Long, ControlSignal> sender) {
        this.sender = sender;
    }

    @Bean
    public EntityListenerImplementation<Long, ControlSignal> controlSignalListenerImplementation() {
        return new EntityListenerImplementation<>(sender, topic);
    }
}
