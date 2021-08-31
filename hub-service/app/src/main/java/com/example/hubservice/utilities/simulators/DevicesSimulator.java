package com.example.hubservice.utilities.simulators;

import com.example.hubservice.management.hub.model.Device;
import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubManagementService;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Component
public class DevicesSimulator {
    private final HubManagementService hubManagementService;
    private final Mqtt5AsyncClient client;
    private final String mqttServerHost = "mqtt";
    private final int mqttServerPort = 8883;

    private final String[] logValues = {"OK", "NOT_OK"};
    private static final String[] measurementTypes = {"temperature_[K]", "humidity_[%]", "pressure_[hPa]"};


    private final Logger logger = LoggerFactory.getLogger(DevicesSimulator.class);

    public DevicesSimulator(HubManagementService hubManagementService) {
        this.hubManagementService = hubManagementService;

        client = Mqtt5Client.builder()
                .identifier(hubManagementService.getHubId() + "_simulation")
                .serverHost(mqttServerHost)
                .serverPort(mqttServerPort)
                .buildAsync();

        client.connect()
                .whenComplete(((mqtt5ConnAck, throwable) -> {
                    if (throwable == null) {
                        logger.info("Simulator connected successfully");
                    }
                    else logger.info("Simulator connected unsuccessfully: " + throwable.getMessage());
                }));
    }

    @Scheduled(fixedRate = 3000)
    public void sendData() {
        Device device = hubManagementService.getRandomDevice();
        if (device != null) {
            String measurementType = randomElement(measurementTypes);
            String topic = "devices/" + device.getId() + "/data/" + measurementType;
            String valueAsDouble = String.valueOf(100.0D * (new Random().nextDouble()));

            publishMessage(topic, valueAsDouble);
        }
        else logger.info("Devices are empty");
    }

    @Bean
    public CompletableFuture<Mqtt5SubAck> subscribeToDeviceControlSignals() {
        String topic = "devices/+/logs/in";

        return client.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(mqtt5Publish -> {
                    logger.info("Received " + mqtt5Publish);

                    List<String> topicLevels = mqtt5Publish.getTopic().getLevels();
                    String deviceId = topicLevels.get(1);

                    String responseLog = randomElement(logValues);
                    String responseTopic = "devices/" + deviceId + "/logs/out";

                    publishMessage(responseTopic, responseLog);
                })
                .send()
                .whenComplete((mqtt5SubAck, throwable) -> {
                    if (throwable == null)
                        logger.info("Successfully subscribed to: " + topic);
                    else logger.info("Connected unsuccessfully to: " + topic + ", error: " + throwable.getMessage());
                });
    }

    private CompletableFuture<Mqtt5PublishResult> publishMessage(String topic, String message) {
        return client.publishWith()
                .topic(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(message.getBytes())
                .send()
                .whenComplete((mqtt5PublishResult, throwable) -> {
                    if (throwable == null)
                        logger.info("Sent " + message + " on topic " + topic + " with result " + mqtt5PublishResult);
                    else logger.info("Sending unsuccessful: " + throwable.getMessage());
                });
    }

    private <T> T randomElement(T[] array) {
        return array[new Random().nextInt(array.length)];
    }
}
