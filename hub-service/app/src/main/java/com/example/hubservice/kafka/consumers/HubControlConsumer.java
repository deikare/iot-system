package com.example.hubservice.kafka.consumers;

import com.example.hubservice.kafka.record.control.hub.KafkaHubControlRecordWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HubControlConsumer {
    Logger logger = LoggerFactory.getLogger(HubControlConsumer.class);

    @KafkaListener(topics = "sent_hub_controls", containerFactory = "hubControlKafkaListenerContainerFactory")
    public void listen(KafkaHubControlRecordWrapper data) {
        logger.info(data.toString());
    }
}