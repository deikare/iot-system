package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.LogseriesRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.LogseriesRepresentationModel;
import com.example.backend.data.model.logseries.DeviceLogseries;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("logs")
public class LogController {
    private final InfluxQueryService influxQueryService;
    private final LogseriesRepresentationAssembler assembler;

    private final String measurement = "deviceLog";
    private final String field = "value";
    private final String bucket = "logs";

    public LogController(InfluxQueryService influxQueryService, LogseriesRepresentationAssembler assembler) {
        this.influxQueryService = influxQueryService;
        this.assembler = assembler;
    }


    @GetMapping
    public LogseriesRepresentationModel all(
                @RequestParam(required = false) Instant start,
                @RequestParam(required = false) Instant stop,
        @RequestParam(defaultValue = "true") boolean desc,
        @RequestParam(required = false) Long limit,
        @RequestParam(required = false) List<String> hubIds,
        @RequestParam(required = false) List<String> deviceIds) {
        String fluxQuery = influxQueryService.produceQuery(bucket, measurement, field, start, stop, desc, hubIds, deviceIds);
        DeviceLogseries<InfluxDeviceLogPojo> queryResult = influxQueryService.queryWithResultMappedToLogseries(fluxQuery, limit, InfluxDeviceLogPojo.class);

        return assembler.toModelConsideringQueryParams(queryResult, limit, hubIds, deviceIds);
    }
}

