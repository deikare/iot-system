package com.example.hubservice.kafka.consumers;

import com.example.hubservice.kafka.record.crud.KafkaHubConfigurationRecordWrapper;
import com.example.hubservice.kafka.record.crud.OperationType;
import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HubConfigurationConsumer {
    Logger logger = LoggerFactory.getLogger(HubConfigurationConsumer.class);

    private final HubManagementService hubManagementService;

    public HubConfigurationConsumer(HubManagementService hubManagementService) {
        this.hubManagementService = hubManagementService;
    }

    @KafkaListener(topics = "hubs", containerFactory = "hubConfigurationKafkaListenerContainerFactory")
    public void listen(KafkaHubConfigurationRecordWrapper data) {
        logger.info("Received: " + data.toString());

        switch (data.getOperationType()) {
            case CREATE -> logger.info("Received: " + OperationType.CREATE + " of already created hub");

            case UPDATE -> logger.info("Updated: " + hubManagementService.updateStack(data.getObject()));

            case DELETE -> hubManagementService.initiateShutdownOnDelete();
        }

    }
}
