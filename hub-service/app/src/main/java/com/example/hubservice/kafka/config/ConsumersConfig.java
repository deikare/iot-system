package com.example.hubservice.kafka.config;

import com.example.hubservice.kafka.record.control.hub.KafkaHubControlRecordWrapper;
import com.example.hubservice.kafka.record.crud.KafkaHubConfigurationRecordWrapper;
import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubManagementService;
import com.example.hubservice.utilities.loggers.abstracts.ConfigLogger;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableKafka
public class ConsumersConfig {
    private final Logger logger = LoggerFactory.getLogger(ConsumersConfig.class);

    private final HubManagementService hubManagementService;

    private final String serverFQDN = "iot.switzerlandnorth.cloudapp.azure.com"; //TODO get kafka adress from env variable
    private final String [] ports = {"9092", "9093", "9094"};
    private final String bootstrapServers = getServerListWithPorts(serverFQDN, ports);

    public ConsumersConfig(HubManagementService hubManagementService) {
        this.hubManagementService = hubManagementService;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, ControlSignal> deviceControlKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, ControlSignal> result =
                new ConcurrentKafkaListenerContainerFactory<>();

        result.setConsumerFactory(deviceControlConsumerFactory());
        result.setConcurrency(1);
        result.setRecordFilterStrategy(consumerRecord -> {
            boolean filterResult = !hubManagementService.isControlSignalPresent(consumerRecord.key());
            logger.info("Filtering message: " + consumerRecord + ", discard? = " + filterResult);
            return filterResult;
        });

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public ConsumerFactory<Long, ControlSignal> deviceControlConsumerFactory() {
        JsonDeserializer<ControlSignal> deserializer =  new JsonDeserializer<>(ControlSignal.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> properties = new HashMap<>();

        properties.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, hubManagementService.getHubId() + "_" + "hub_configuration");

        properties.put("security.protocol", "SSL");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/secrets/kafka-client/kafka.client.truststore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/secrets/kafka-client/kafka.client.keystore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG, "confluent");

        ConfigLogger.produceConfigBeanCreationLog(logger, properties);

        ConsumerFactory<Long, ControlSignal> result = new DefaultKafkaConsumerFactory<>(properties, new LongDeserializer(), deserializer);
        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaHubControlRecordWrapper> hubControlKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaHubControlRecordWrapper> result =
                new ConcurrentKafkaListenerContainerFactory<>();

        result.setConsumerFactory(hubControlConsumerFactory());
        result.setConcurrency(1);
        result.setRecordFilterStrategy(consumerRecord -> {
            String dataIdFromKey = consumerRecord.key();
            String dataIdFromValue = consumerRecord.value().getHub().getId();

            boolean filterResult = !hubManagementService.isHubPresent(dataIdFromKey) && hubManagementService.isHubPresent(dataIdFromValue);
            logger.info("Filtering message: " + consumerRecord + ", discard? = " + filterResult);
            return filterResult;
        });

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public ConsumerFactory<String, KafkaHubControlRecordWrapper> hubControlConsumerFactory() {
        JsonDeserializer<KafkaHubControlRecordWrapper> deserializer =  new JsonDeserializer<>(KafkaHubControlRecordWrapper.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> properties = new HashMap<>();

        properties.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, hubManagementService.getHubId() + "_" + "hub_configuration");

        properties.put("security.protocol", "SSL");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/secrets/kafka-client/kafka.client.truststore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/secrets/kafka-client/kafka.client.keystore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG, "confluent");

        ConfigLogger.produceConfigBeanCreationLog(logger, properties);

        ConsumerFactory<String, KafkaHubControlRecordWrapper> result = new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), deserializer);
        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaHubConfigurationRecordWrapper> hubConfigurationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaHubConfigurationRecordWrapper> result =
                new ConcurrentKafkaListenerContainerFactory<>();

        result.setConsumerFactory(hubConfigurationConsumerFactory());
        result.setConcurrency(1);
        result.setRecordFilterStrategy(consumerRecord -> {
            String dataIdFromKey = consumerRecord.key();
            String dataIdFromValue = consumerRecord.value().getObject().getId();

            boolean filterResult = !hubManagementService.isHubPresent(dataIdFromKey) && hubManagementService.isHubPresent(dataIdFromValue);
            logger.info("Filtering message: " + consumerRecord + ", discard? = " + filterResult);
            return filterResult;
        });

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public ConsumerFactory<String, KafkaHubConfigurationRecordWrapper> hubConfigurationConsumerFactory() {
        JsonDeserializer<KafkaHubConfigurationRecordWrapper> deserializer =  new JsonDeserializer<>(KafkaHubConfigurationRecordWrapper.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> properties = new HashMap<>();

        properties.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, hubManagementService.getHubId() + "_" + "hub_configuration");

        properties.put("security.protocol", "SSL");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/secrets/kafka-client/kafka.client.truststore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/secrets/kafka-client/kafka.client.keystore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG, "confluent");

        ConfigLogger.produceConfigBeanCreationLog(logger, properties);

        ConsumerFactory<String, KafkaHubConfigurationRecordWrapper> result = new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), deserializer);
        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    private String getServerListWithPorts(String serverFQDN, String[] ports) {
        StringBuilder result = new StringBuilder();

        for (String port : ports)
            result = result.append(serverFQDN).append(":").append(port).append(",");

        return Objects.requireNonNull(result).substring(0, result.length() - 1);
    }
}
