package com.example.backend.device.manager.kafka.services.senders.config;

import com.example.backend.device.manager.kafka.producer.KafkaControlSignalProducer;
import com.example.backend.device.manager.kafka.services.senders.ControlSignalSenderService;
import com.example.backend.device.manager.kafka.services.senders.EntityCrudSenderService;
import com.example.backend.device.manager.model.Hub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class ControlSignalSenderServiceConfiguration {
    private final KafkaControlSignalProducer sender;
    private final String topic = "sent_controls";

    public ControlSignalSenderServiceConfiguration(KafkaControlSignalProducer sender) {
        this.sender = sender;
    }

    @Bean
    public ControlSignalSenderService controlSignalSenderService() {
        return new ControlSignalSenderService(sender, topic);
    }
}
