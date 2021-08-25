package com.example.backend.device.manager.kafka.services.senders.config;

import com.example.backend.device.manager.kafka.producer.KafkaCrudEntityProducer;
import com.example.backend.device.manager.kafka.record.KafkaEntityControlRecordWrapper;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.kafka.services.senders.EntityCrudSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubEntityCrudSenderServiceConfiguration {
    private final KafkaCrudEntityProducer<String, KafkaEntityControlRecordWrapper<Hub>> sender;
    private final String topic = "hubs";

    public HubEntityCrudSenderServiceConfiguration(KafkaCrudEntityProducer<String, KafkaEntityControlRecordWrapper<Hub>> sender) {
        this.sender = sender;
    }

    @Bean
    public EntityCrudSenderService<String, Hub> hubEntityCrudSenderService() {
        return new EntityCrudSenderService<>(sender, topic);
    }
}
