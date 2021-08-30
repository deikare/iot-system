package com.example.hubservice.kafka.consumers;

import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubManagementService;
import com.example.hubservice.mqtt.MqttClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeviceControlConsumer {
    private final MqttClientService mqttClientService;
    private final HubManagementService hubManagementService;
    private final Logger logger = LoggerFactory.getLogger(DeviceControlConsumer.class);

    public DeviceControlConsumer(MqttClientService mqttClientService, HubManagementService hubManagementService) {
        this.mqttClientService = mqttClientService;
        this.hubManagementService = hubManagementService;
    }

    @KafkaListener(topics = "sent_controls", containerFactory = "deviceControlKafkaListenerContainerFactory")
    public void listen(ControlSignal data) {
        logger.info("Received controlSignal: " + data.toString());

        ControlSignal controlSignal = hubManagementService.getControlSignal(data.getId());

        String topic = produceTopic(controlSignal);
        String message = controlSignal.getMessageContent();

        logger.info("Trying to send message: topic = " + topic + ", content = " + message);
        mqttClientService.publishMessage(topic, message);
    }

    private String produceTopic(ControlSignal data) {
        return "devices/" + data.getDevice().getId() + "/in";
    }
}