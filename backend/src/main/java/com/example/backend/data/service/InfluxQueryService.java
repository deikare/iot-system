package com.example.backend.data.service;

import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceValueInterface;
import com.influxdb.client.QueryApi;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InfluxQueryService {
    private final Logger logger = LoggerFactory.getLogger(InfluxQueryService.class);

    private final QueryApi queryApi;

    public InfluxQueryService(QueryApi queryApi) {
        this.queryApi = queryApi;
    }

    public <V, I extends InfluxDeviceValueInterface<V>> DeviceBaseTimeseriesList<V, I> queryWithResultMappedToTimeseriesList(String flux, Class<I> classToMapRecords) {
        List<I> rawResult = query(flux, classToMapRecords);
        DeviceBaseTimeseriesList<V, I> timeseriesList = new DeviceBaseTimeseriesList<>(rawResult);
        logger.info("Query result as timeseriesList: " + timeseriesList);
        return timeseriesList;
    }

    public <I> List<I> query(String flux, Class<I> classToMapRecords) {
        logger.info("Starting query: " + flux);
        List<I> result = queryApi.query(flux, classToMapRecords);
        logger.info("Query result: " + result);
        return result;
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
        logger.info("Created query: " + result);
        return result;
    }

    private String addTagToQuery(String tagName, String tagValue) {
        return (tagValue != null && !(tagValue.isEmpty())? " and r." + tagName + " == \"" + tagValue + "\"": "");
    }

    private String addFunctionToQuery(String functionName, String functionBody) {
        return (functionBody != null && (!functionBody.isEmpty())? "|> " + functionName + "(" + functionBody + ")" : "");
    }
}
