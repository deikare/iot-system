package com.example.hubservice.kafka.config;

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
public class ConsumerConfig {
    private final Logger logger = LoggerFactory.getLogger(ConsumerConfig.class);

    private final String serverFQDN = "iot.switzerlandnorth.cloudapp.azure.com";
    private final String [] ports = {"9092", "9093", "9094"};
    private final String bootstrapServers = getServerListWithPorts(serverFQDN, ports);

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> result =
                new ConcurrentKafkaListenerContainerFactory<>();
        result.setConsumerFactory(consumerFactory());
        result.setConcurrency(1);

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        ConsumerFactory<String, String> result = new DefaultKafkaConsumerFactory<>(hubProperties());
        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public Map<String, Object> hubProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        result.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        result.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        result.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "lol");

        result.put("security.protocol", "SSL");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/deikare/wut/iot-system/hub-service/certs/kafka-client/kafka.client.truststore.jks");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "confluent");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/home/deikare/wut/iot-system/hub-service/certs/kafka-client/kafka.client.keystore.jks");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "confluent");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG, "confluent");

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public Map<String, Object> othersProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        result.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        result.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        result.put("security.protocol", "SSL");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/deikare/wut/iot-system/hub-service/certs/kafka-client/kafka.client.truststore.jks");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "confluent");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/home/deikare/wut/iot-system/hub-service/certs/kafka-client/kafka.client.keystore.jks");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "confluent");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        result.put(org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG, "confluent");

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    private String getServerListWithPorts(String serverFQDN, String[] ports) {
        StringBuilder result = new StringBuilder("");

        for (String port : ports)
            result = result.append(serverFQDN).append(":").append(port).append(",");

        return Objects.requireNonNull(result).substring(0, result.length() - 1);
    }
}
