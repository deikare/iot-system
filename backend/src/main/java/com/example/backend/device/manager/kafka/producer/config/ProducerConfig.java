package com.example.backend.device.manager.kafka.producer.config;

import com.example.backend.device.manager.kafka.producer.KafkaControlSignalProducer;
import com.example.backend.device.manager.kafka.producer.KafkaCrudEntityProducer;
import com.example.backend.device.manager.kafka.record.KafkaCrudRecordWrapper;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfig {
    private final Logger logger = LoggerFactory.getLogger(ProducerConfig.class);

    @Bean
    public KafkaCrudEntityProducer<String, KafkaCrudRecordWrapper<Hub>> hubKafkaCrudEntityProducer() {
        KafkaCrudEntityProducer<String, KafkaCrudRecordWrapper<Hub>> result = new KafkaCrudEntityProducer<>(hubKafkaTemplate());
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "HubKafkaCrudEntitySender");
        return result;
    }

    @Bean
    public KafkaTemplate<String, KafkaCrudRecordWrapper<Hub>> hubKafkaTemplate() {
        KafkaTemplate<String, KafkaCrudRecordWrapper<Hub>> result = new KafkaTemplate<>(hubProducerFactory());
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "HubKafkaTemplate");
        return result;
    }

    @Bean
    public ProducerFactory<String, KafkaCrudRecordWrapper<Hub>> hubProducerFactory() {
        ProducerFactory<String, KafkaCrudRecordWrapper<Hub>> result = new DefaultKafkaProducerFactory<>(hubProperties());
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "HubProducerFactory");
        return result;
    }


    //for control signal sending
    @Bean
    public KafkaControlSignalProducer controlSignalProducer() {
        KafkaControlSignalProducer result = new KafkaControlSignalProducer(controlSignalSenderKafkaTemplate());
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalSender");
        return result;
    }

    @Bean
    public KafkaTemplate<Long, ControlSignal> controlSignalSenderKafkaTemplate() {
        KafkaTemplate<Long, ControlSignal> result = new KafkaTemplate<>(controlSignalSenderProducerFactory());
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalSenderKafkaTemplate");
        return result;
    }

    @Bean
    public ProducerFactory<Long, ControlSignal> controlSignalSenderProducerFactory() {
        ProducerFactory<Long, ControlSignal> result = new DefaultKafkaProducerFactory<>(othersProperties());
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalSenderProducerFactory");
        return result;
    }

    @Bean
    public Map<String, Object> hubProperties() {
        Map<String, Object> result = new HashMap<>();

        String bootstrapServer = "localhost:8092";

        result.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        result.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        result.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        result.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, "all");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        result.put(org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        result.put(org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, "20");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }

    @Bean
    public Map<String, Object> othersProperties() {
        Map<String, Object> result = new HashMap<>();

        String bootstrapServer = "localhost:8092";

        result.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        result.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        result.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        result.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, "all");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        result.put(org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        result.put(org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, "20");
        result.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));

        ConfigLogger.produceConfigBeanCreationLog(logger, result);

        return result;
    }
}
