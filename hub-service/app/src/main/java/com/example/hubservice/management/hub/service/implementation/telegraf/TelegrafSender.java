package com.example.hubservice.management.hub.service.implementation.telegraf;

import com.example.hubservice.management.hub.model.Hub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;

@Service
public class TelegrafSender {
    private final String bucketTagName = "bucketName";
    private final String telegrafServerCall = "http://telegraf:8086/api/v2/write?precision=ms";

    private final Logger logger = LoggerFactory.getLogger(TelegrafSender.class);

    public TelegrafSender() {
        logger.info("Creating Telegraf sender");
    }

    public void sendHubLogToTelegraf(Hub hub) {
        HashMap<String, String> tags = new HashMap<>();
        tags.put("hubId", hub.getId());
        tags.put("name", hub.getName());

        String writeQuery = produceInfluxLineProtocolPoint("hubs", "hubLog", Instant.now().toEpochMilli(),
                "value", true, hub.getStatus().name(), tags);

        sendWriteQueryToTelegraf(writeQuery);
    }

    public void sendDeviceDataToTelegraf(Hub hub, String deviceIdAsString, String measurementType, String dataAsString) {
        HashMap<String, String> tags = new HashMap<>();
        tags.put("hubId", hub.getId());
        tags.put("deviceId", deviceIdAsString);
        tags.put("type", measurementType);

        String writeQuery = produceInfluxLineProtocolPoint("data", "deviceData", Instant.now().toEpochMilli(),
                "value", false, dataAsString, tags);

        sendWriteQueryToTelegraf(writeQuery);
    }

    public void sendDeviceLogToTelegraf(Hub hub, String deviceIdAsString, String log) {
        HashMap<String, String> tags = new HashMap<>();
        tags.put("hubId", hub.getId());
        tags.put("deviceId", String.valueOf(deviceIdAsString));

        String writeQuery = produceInfluxLineProtocolPoint("logs", "deviceLog", Instant.now().toEpochMilli(),
                "value", true, log, tags);

        sendWriteQueryToTelegraf(writeQuery);
    }

    private void sendWriteQueryToTelegraf(String writeQuery) {
        logger.info("Sending writeQuery to Telegraf: " + writeQuery);
        new RestTemplate().postForLocation(telegrafServerCall, writeQuery);
    }
    //TODO add sqlite

    public String produceInfluxLineProtocolPoint(String bucket, String measurement, Long msTimestamp, String fieldKey, boolean isValueString, String fieldValueAsString, HashMap<String, String> tagPairs) {
        StringBuilder result = new StringBuilder(measurement + "," + bucketTagName + "=" + bucket);

        for (var pair : tagPairs.entrySet())
            result.append(",").append(pair.getKey()).append("=").append(pair.getValue());

        return result + " "
                + fieldKey + "=" + (isValueString? ("\"" + fieldValueAsString + "\"") : fieldValueAsString)
                + " " + msTimestamp.toString();
    }
}
