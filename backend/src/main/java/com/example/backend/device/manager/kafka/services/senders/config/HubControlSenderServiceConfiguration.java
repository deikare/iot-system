package com.example.backend.device.manager.kafka.services.senders.config;

import com.example.backend.device.manager.kafka.producer.KafkaHubControlProducer;
import com.example.backend.device.manager.kafka.services.senders.HubControlSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubControlSenderServiceConfiguration {
    private final KafkaHubControlProducer sender;
    private final String topic = "sent_hub_controls";

    public HubControlSenderServiceConfiguration(KafkaHubControlProducer sender) {
        this.sender = sender;
    }

    @Bean
    public HubControlSenderService hubControlSenderService() {
        return new HubControlSenderService(sender, topic);
    }
}
