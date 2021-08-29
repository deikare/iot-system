package com.example.hubservice.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {
    Logger logger = LoggerFactory.getLogger(TestConsumer.class);

    @KafkaListener(id = "foo", topics = "xD", clientIdPrefix = "myClientId")
    public void listen(String data) {
        logger.info(data);
    }
}
