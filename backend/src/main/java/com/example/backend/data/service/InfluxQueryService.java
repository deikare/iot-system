package com.example.backend.data.service;

import com.influxdb.client.QueryApi;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
//TODO may add pagination based on start, stop and limit flux functions
public class InfluxQueryService {
    private final Logger logger = LoggerFactory.getLogger(InfluxQueryService.class);

    private final QueryApi queryApi;

    public InfluxQueryService(QueryApi queryApi) {
        this.queryApi = queryApi;
    }

    public <M> List<M> query(String flux, Class<M> classToMapRecords) {
        logger.info("Starting query");
        return queryApi.query(flux, classToMapRecords);
    }

    public String produceQuery(@NotNull String bucket, @NotNull String range, @NotNull String measurement,
                               @NotNull String field, String hubId, String deviceId, String type, String sort, String limit,
                               String aggregateWindow) {
        String result = "from(bucket: \"" + bucket + "\") " +
                addFunctionToQuery("range", range) + " " +
                "|> filter(fn: (r) => r._measurement == \"" + measurement + "\"" +
                addTagToQuery("_field", field).stripTrailing() +
                addTagToQuery("hubId", hubId).stripTrailing() +
                addTagToQuery("deviceId", deviceId).stripTrailing() +
                addTagToQuery("type", type).stripTrailing() + ") " +
                addFunctionToQuery("sort", sort).stripTrailing() + " " +
                addFunctionToQuery("limit", limit).stripTrailing() + " " +
                addFunctionToQuery("aggregateWindow", aggregateWindow).stripTrailing();
        logger.info("Producing query: " + result);
        return result;
    }

    private String addTagToQuery(String tagName, String tagValue) {
        return (tagValue != null && !(tagValue.isEmpty())? " and r." + tagName + " == \"" + tagValue + "\"": "");
    }

    private String addFunctionToQuery(String functionName, String functionBody) {
        return (functionBody != null && (!functionBody.isEmpty())? "|> " + functionName + "(" + functionBody + ")" : "");
    }
}
