package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.LogTimeseriesListRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.LogTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "start: 0") String range,
            @RequestParam(required = false) String hubId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String logType,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String aggregateWindow) {
        String fluxQuery = influxQueryService.produceQuery(bucket, range, measurement, field, hubId, deviceId, logType, sort, limit, aggregateWindow);
        DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> queryResult = influxQueryService.queryWithResultMappedToTimeseriesList(fluxQuery, InfluxDeviceLogPojo.class);

        return assembler.toModel(queryResult);
    }
}

