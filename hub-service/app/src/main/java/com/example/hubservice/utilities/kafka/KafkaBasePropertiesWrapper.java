package com.example.hubservice.utilities.kafka;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KafkaBasePropertiesWrapper {
    private final Map<String, Object> properties;

    private final String serverFQDN = "iot.switzerlandnorth.cloudapp.azure.com";
    private final String [] ports = {"9092", "9093", "9094"};
    private final String bootstrapServers = getServerListWithPorts(serverFQDN, ports);


    public KafkaBasePropertiesWrapper() {
        properties = new HashMap<>();

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        properties.put("security.protocol", "SSL");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/deikare/wut/iot-system/hub-service/certs/kafka-client/kafka.client.truststore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/home/deikare/wut/iot-system/hub-service/certs/kafka-client/kafka.client.keystore.jks");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "confluent");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        properties.put(org.apache.kafka.common.config.SslConfigs.SSL_KEY_PASSWORD_CONFIG, "confluent");
    }

    private String getServerListWithPorts(String serverFQDN, String[] ports) {
        StringBuilder result = new StringBuilder("");

        for (String port : ports)
            result = result.append(serverFQDN).append(":").append(port).append(",");

        return Objects.requireNonNull(result).substring(0, result.length() - 1);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "KafkaBasePropertiesWrapper{" +
                "properties=" + properties +
                '}';
    }
}
