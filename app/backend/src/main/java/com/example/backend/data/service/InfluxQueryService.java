package com.example.backend.data.service;

import com.influxdb.client.QueryApi;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfluxQueryService {
    private final QueryApi queryApi;

    public InfluxQueryService(QueryApi queryApi) {
        this.queryApi = queryApi;
    }

    public <M> List<M> query(String flux, Class<M> measurementType) {
        return queryApi.query(flux, measurementType);
    }

    public String produceQuery(@NotNull String bucket, @NotNull String range, @NotNull String measurement,
                               @NotNull String field, String hubId, String deviceId, String sort, String limit,
                               String aggregateWindow) {
        return "data = from(bucket: \"" + bucket + "\") " +
                addFunctionToQuery("range", range) + " " +
                "|> filter(fn: (r) => r._measurement == \"" + measurement + "\" " +
                "and r._field == \"" + field + "\" " +
                addTagToQuery("hubId", hubId) +
                addTagToQuery("deviceId", deviceId) + ") " +
                addFunctionToQuery("sort", sort) + " " +
                addFunctionToQuery("limit", limit) + " " +
                addFunctionToQuery("aggregateWindow", aggregateWindow);
    }

    private String addTagToQuery(String tagName, String tagValue) {
        return (tagValue.isEmpty()? "and r." + tagName + " == \"" + tagValue + "\" ": "");
    }

    private String addFunctionToQuery(String functionName, String functionBody) {
        return (functionBody.isEmpty()? "|> " + functionName + "(" + functionBody + ")" : "");
    }
}
