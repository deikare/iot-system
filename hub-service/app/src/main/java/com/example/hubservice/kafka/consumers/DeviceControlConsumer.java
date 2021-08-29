package com.example.hubservice.kafka.consumers;

import com.example.hubservice.management.hub.model.ControlSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeviceControlConsumer {
    Logger logger = LoggerFactory.getLogger(DeviceControlConsumer.class);

    @KafkaListener(topics = "sent_controls", containerFactory = "hubControlKafkaListenerContainerFactory")
    public void listen(ControlSignal data) {
        logger.info(data.toString());
    }
}