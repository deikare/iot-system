package com.example.hubservice.mqtt;

import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubManagementService;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MqttClientService {
    private final Mqtt5AsyncClient client;
    private final HubManagementService hubManagementService;
    private final String mqttServerHost = "localhost";
    private final int mqttServerPort = 8883;

    private final Logger logger = LoggerFactory.getLogger(MqttClientService.class);

    public MqttClientService(HubManagementService hubManagementService) {
        this.hubManagementService = hubManagementService;

        client = Mqtt5Client.builder()
                .identifier(hubManagementService.getHubId())
                .serverHost(mqttServerHost)
                .serverPort(mqttServerPort)
                .buildAsync();

        client.connect()
                        .whenComplete(((mqtt5ConnAck, throwable) -> {
                            if (throwable == null) {
                                logger.info("Connected successfully");
                            }
                            else logger.info("Connected unsuccessfully: " + throwable.getMessage());
                        }));

        logger.info("Created client: " + client);
    }

    public CompletableFuture<Mqtt5PublishResult> publishMessage(String topic, String message) {
        return client.publishWith()
                .topic(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(message.getBytes())
                .send()
                .whenComplete((mqtt5PublishResult, throwable) -> {
                    if (throwable == null) {
                        logger.info("Sent " + message + " on topic " + topic + " with result " + mqtt5PublishResult);
                    }
                    else logger.info("Sending unsuccessful: " + throwable.getMessage());
                });
    }
}
