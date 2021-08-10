package com.example.backend.device.manager.model.listeners.config;

import com.example.backend.device.manager.kafka.producer.KafkaCrudEntitySender;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceEntityListenerConfiguration {
    private final KafkaCrudEntitySender<Long, Device> sender;
    private final String topic = "devices";

    public DeviceEntityListenerConfiguration(KafkaCrudEntitySender<Long, Device> sender) {
        this.sender = sender;
    }


    @Bean
    public EntityListenerImplementation<Long, Device> deviceListenerImplementation() {
        return new EntityListenerImplementation<>(sender, topic);
    }
}
