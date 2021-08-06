package com.example.backend.device.manager.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class HubKafkaConfig {
    private final String bootstrapServer = "localhost:8092";

    @Bean
    public KafkaTemplate<Long, String> HubKafkaTemplate() {
        return new KafkaTemplate<>(hubProducerFactory());
    }

    @Bean
    public ProducerFactory<Long, String> hubProducerFactory() {
        return new DefaultKafkaProducerFactory<>(hubProducerConfigs());
    }

    @Bean
    public Map<String, Object> hubProducerConfigs() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        return properties;
    }
}
