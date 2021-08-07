package com.example.backend.device.manager.model.listeners.config;

import com.example.backend.device.manager.kafka.producer.KafkaEntitySender;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceEntityListenerConfiguration {
    private final KafkaEntitySender<Long, Device> sender;
    private final String topic = "devices";

    public DeviceEntityListenerConfiguration(KafkaEntitySender<Long, Device> sender) {
        this.sender = sender;
    }


    @Bean
    public EntityListenerImplementation<Long, Device> deviceListenerImplementation() {
        return new EntityListenerImplementation<>(sender, topic);
    }
}
