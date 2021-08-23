package com.example.backend.data.service;

import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceValueInterface;
import com.influxdb.client.reactive.QueryReactiveApi;
import com.influxdb.query.dsl.Flux;
import com.influxdb.query.dsl.functions.restriction.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InfluxQueryService {
    private final Logger logger = LoggerFactory.getLogger(InfluxQueryService.class);

    private final QueryReactiveApi queryReactiveApi;

    public InfluxQueryService(QueryReactiveApi queryReactiveApi) {
        this.queryReactiveApi = queryReactiveApi;
    }

    public <V, I extends InfluxDeviceValueInterface<V>> DeviceBaseTimeseriesList<V, I> queryWithResultMappedToTimeseriesList(String flux, Long limit, Class<I> classToMapRawRecords) {
        List<I> rawResult = query(flux, limit, classToMapRawRecords);
        DeviceBaseTimeseriesList<V, I> timeseriesList = new DeviceBaseTimeseriesList<>(rawResult);
        logger.info("Query result as timeseriesList: " + timeseriesList);
        return timeseriesList;
    }

    public <I> List<I> query(String flux, Long limit, Class<I> classToMapRawRecords) {
        logger.info("Starting query: " + flux);
        List<I> result = new ArrayList<>();

        if (limit == null)
            queryReactiveApi.query(flux, classToMapRawRecords).subscribe(result::add);
        else queryReactiveApi.query(flux, classToMapRawRecords).take(limit).subscribe(result::add);

        logger.info("Query result: " + result);
        return result;
    }

    public String produceQuery(@NotNull String bucket, @NotNull String measurement,
                               @NotNull String field, Instant start, Instant stop, boolean desc,
                               String hubId, String deviceId, String type) {

        Restrictions restrictions = generateRestrictions(measurement, field, hubId, deviceId, type);
        String result = generateQueryFromRestrictions(bucket, restrictions, start, stop, desc);

        logger.info("Created query: " + result);
        return result;
    }

    public String produceQuery(@NotNull String bucket, @NotNull String measurement,
                               @NotNull String field, Instant start, Instant stop, boolean desc,
                               List<String> hubIds, List<String> deviceIds, List<String> types) {

        Restrictions restrictions = generateRestrictions(measurement, field, hubIds, deviceIds, types);
        String result = generateQueryFromRestrictions(bucket, restrictions, start, stop, desc);

        logger.info("Created query: " + result);
        return result;
    }

    private Restrictions generateRestrictions(@NotNull String measurement, @NotNull String field, List<String> hubIds, List<String> deviceIds, List<String> types) {
        Restrictions restrictions = generateBaseRestrictions(measurement, field);

        restrictions = addTagListRestrictions(restrictions, "hubId", hubIds);
        restrictions = addTagListRestrictions(restrictions, "deviceId", deviceIds);
        restrictions = addTagListRestrictions(restrictions, "type", types);

        return restrictions;
    }

    private Restrictions generateRestrictions(@NotNull String measurement, @NotNull String field, String hubId, String deviceId, String type) {
        Restrictions restrictions = generateBaseRestrictions(measurement, field);

        restrictions = addTagRestriction(restrictions, "hubId", hubId);
        restrictions = addTagRestriction(restrictions, "deviceId", deviceId);
        restrictions = addTagRestriction(restrictions, "type", type);

        return restrictions;
    }

    private Restrictions generateBaseRestrictions(@NotNull String measurement, @NotNull String field) {
        return Restrictions.and(
                Restrictions.measurement().equal(measurement),
                Restrictions.field().equal(field),
                Restrictions.value().exists()
        );
    }

    private Restrictions addTagRestriction(Restrictions baseRestrictions, String tagName, String tagValue) {
        Restrictions result;

        if (tagName != null && !tagName.isEmpty() && tagValue != null && !tagValue.isEmpty())
            result = Restrictions.and(baseRestrictions, generateTagRestrictions(tagName, tagValue));
        else result = baseRestrictions;

        return result;
    }

    private Restrictions addTagListRestrictions(Restrictions baseRestrictions, String tagName, List<String> tagValues) {
        Restrictions result;

        if (tagName != null && !tagName.isEmpty() && tagValues != null && !tagValues.isEmpty())
            result = Restrictions.and(baseRestrictions, generateTagListRestrictions(tagName, tagValues));
        else result = baseRestrictions;

        return result;
    }

    private Restrictions generateTagListRestrictions(String tagName, List<String> tagValues) {
        Restrictions result = null;
        if (tagName != null && !tagName.isEmpty() && tagValues != null && !tagValues.isEmpty()) {
            result = Restrictions.or();

            for (String tagValue : tagValues)
                result = Restrictions.or(result,
                        generateTagRestrictions(tagName, tagValue));
        }
        return result;
    }

    private Restrictions generateTagRestrictions(String tagName, String tagValue) {
        Restrictions result = null;

        if (tagName != null && !tagName.isEmpty() && tagValue != null && !tagValue.isEmpty())
            result = Restrictions.tag(tagName).equal(tagValue);

        return result;
    }

    private String generateQueryFromRestrictions(String bucket, Restrictions restrictions, Instant start, Instant stop,
                                                 boolean desc) {
        Flux flux = Flux.from(bucket);

        if (start == null) {
            if (stop == null)
                flux = flux.range(Instant.now().minus(2, ChronoUnit.HOURS));
            else flux = flux.range(stop.minus(2, ChronoUnit.HOURS), stop);
        }
        else {
            if (stop == null)
                flux = flux.range(start);
            else flux = flux.range(start, stop);
        }

        flux = flux.filter(restrictions).sort(new String[]{"_time"}, desc);

        return flux.toString();
    }
}
