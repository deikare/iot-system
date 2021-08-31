package com.example.hubservice.kafka.consumers;

import com.example.hubservice.kafka.record.control.hub.KafkaHubControlRecordWrapper;
import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HubControlConsumer {
    Logger logger = LoggerFactory.getLogger(HubControlConsumer.class);

    private final HubManagementService hubManagementService;

    public HubControlConsumer(HubManagementService hubManagementService) {
        this.hubManagementService = hubManagementService;
    }

    @KafkaListener(topics = "sent_hub_controls", containerFactory = "hubControlKafkaListenerContainerFactory")
    public void listen(KafkaHubControlRecordWrapper data) {
        logger.info("Received: " + data.toString());

        hubManagementService.changeHubStatus(data.getControlType());
    }
}