package com.example.backend.device.manager.kafka.topics;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TopicsConfiguration {
    @Bean
    public KafkaAdmin admin() {
        String bootstrapServers = "kafka1:8092,kafka2:8093,kafka3:8094";
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic hubsConfigurationTopic() {
        return TopicBuilder.name("hubs")
                .partitions(10)
                .replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .compact()
                .build();
    }

    @Bean
    public NewTopic hubsControlTopic() {
        return TopicBuilder.name("sent_hub_controls")
                .partitions(10)
                .replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .compact()
                .build();
    }

    @Bean
    public NewTopic deviceControlTopic() {
        return TopicBuilder.name("sent_device_controls")
                .partitions(10)
                .replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .compact()
                .build();
    }
}
