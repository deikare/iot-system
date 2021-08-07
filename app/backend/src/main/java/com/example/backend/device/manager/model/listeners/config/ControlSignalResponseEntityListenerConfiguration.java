package com.example.backend.device.manager.model.listeners.config;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalResponseEntityListenerConfiguration {
    private final KafkaEntitySender<Long, ControlSignalResponse> sender;
    private final String topic = "control-responses";

    public ControlSignalResponseEntityListenerConfiguration(KafkaEntitySender<Long, ControlSignalResponse> sender) {
        this.sender = sender;
    }

    @Bean
    public EntityListenerImplementation<Long, ControlSignalResponse> controlSignalResponseListenerImplementation() {
        return new EntityListenerImplementation<>(sender, topic);
    }
}