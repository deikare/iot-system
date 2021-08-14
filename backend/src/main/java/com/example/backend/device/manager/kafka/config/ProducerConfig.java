package com.example.backend.device.manager.kafka.config;

import com.example.backend.device.manager.kafka.producer.KafkaControlSignalSender;
import com.example.backend.device.manager.kafka.producer.KafkaCrudEntitySender;
import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

//TODO split into for configs based on type {HUB, DEVICE, ...}
@Configuration
public class ProducerConfig {
    @Bean
    public KafkaCrudEntitySender<Long, Hub> hubKafkaCrudEntitySender() {
        return new KafkaCrudEntitySender<>(hubKafkaTemplate());
    }

    @Bean
    public KafkaTemplate<Long, KafkaRecordWrapper<Hub>> hubKafkaTemplate() {
        return new KafkaTemplate<>(hubProducerFactory());
    }

    @Bean
    public ProducerFactory<Long, KafkaRecordWrapper<Hub>> hubProducerFactory() {
        return new DefaultKafkaProducerFactory<>(properties());
    }


    @Bean
    public KafkaCrudEntitySender<Long, Device> deviceKafkaCrudEntitySender() {
        return new KafkaCrudEntitySender<>(deviceKafkaTemplate());
    }

    @Bean
    public KafkaTemplate<Long, KafkaRecordWrapper<Device>> deviceKafkaTemplate() {
        return new KafkaTemplate<>(deviceProducerFactory());
    }

    @Bean
    public ProducerFactory<Long, KafkaRecordWrapper<Device>> deviceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(properties());
    }


    //for control signal sending
    @Bean
    public KafkaControlSignalSender controlSignalSender() {
        return new KafkaControlSignalSender(controlSignalSenderKafkaTemplate());
    }

    @Bean
    public KafkaTemplate<Long, ControlSignal> controlSignalSenderKafkaTemplate() {
        return new KafkaTemplate<>(controlSignalSenderProducerFactory());
    }

    @Bean
    public ProducerFactory<Long, ControlSignal> controlSignalSenderProducerFactory() {
        return new DefaultKafkaProducerFactory<>(properties());
    }


    //for Crud
    @Bean
    public KafkaCrudEntitySender<Long, ControlSignal> controlSignalKafkaCrudEntitySender() {
        return new KafkaCrudEntitySender<>(controlSignalCrudKafkaTemplate());
    }

    @Bean
    public KafkaTemplate<Long, KafkaRecordWrapper<ControlSignal>> controlSignalCrudKafkaTemplate() {
        return new KafkaTemplate<>(controlSignalProducerFactory());
    }

    @Bean
    public ProducerFactory<Long, KafkaRecordWrapper<ControlSignal>> controlSignalProducerFactory() {
        return new DefaultKafkaProducerFactory<>(properties());
    }


    @Bean
    public KafkaCrudEntitySender<Long, ControlSignalResponse> controlSignalResponseCrudKafkaEntitySender() {
        return new KafkaCrudEntitySender<>(controlSignalResponseKafkaTemplate());
    }

    @Bean
    public KafkaTemplate<Long, KafkaRecordWrapper<ControlSignalResponse>> controlSignalResponseKafkaTemplate() {
        return new KafkaTemplate<>(controlSignalResponseProducerFactory());
    }

    @Bean
    public ProducerFactory<Long, KafkaRecordWrapper<ControlSignalResponse>> controlSignalResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(properties());
    }


    @Bean
    public Map<String, Object> properties() {
        Map<String, Object> properties = new HashMap<>();

        String bootstrapServer = "localhost:8092";

        properties.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        properties.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, "all");
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        properties.put(org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));

        return properties;
    }
}
