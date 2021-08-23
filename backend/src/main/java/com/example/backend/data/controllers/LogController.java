package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.LogTimeseriesListRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.LogTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("logs")
public class LogController {
    private final InfluxQueryService influxQueryService;
    private final LogTimeseriesListRepresentationAssembler assembler;
    private final String measurement = "deviceLog";
    private final String field = "value";
    private final String bucket = "logs";

    public LogController(InfluxQueryService influxQueryService, LogTimeseriesListRepresentationAssembler assembler) {
        this.influxQueryService = influxQueryService;
        this.assembler = assembler;
    }


    @GetMapping
    public LogTimeseriesListRepresentationModel all(
                @RequestParam(required = false) Instant start,
                @RequestParam(required = false) Instant stop,
        @RequestParam(defaultValue = "true") boolean desc,
        @RequestParam(defaultValue = "200") Long limit,
        @RequestParam(required = false) List<String> hubIds,
        @RequestParam(required = false) List<String> deviceIds,
        @RequestParam(required = false) List<String> logTypes) {
        String fluxQuery = influxQueryService.produceQuery(bucket, measurement, field, start, stop, desc, hubIds, deviceIds, logTypes);
        DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> queryResult = influxQueryService.queryWithResultMappedToTimeseriesList(fluxQuery, limit, InfluxDeviceLogPojo.class);

        return assembler.toModel(queryResult);
    }
}

