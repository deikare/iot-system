package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.LogRepresentationAssembler;
import com.example.backend.data.controllers.exceptions.InfluxRecordNotFoundException;
import com.example.backend.data.controllers.representation.models.LogRepresentationModel;
import com.example.backend.data.model.InfluxDeviceLogPojo;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("logs")
public class LogController {
    private final InfluxQueryService influxQueryService;
    private final LogRepresentationAssembler assembler;
    private final String measurement = "deviceLog";
    private final String field = "value";
    private final String bucket = "logs";

    public LogController(InfluxQueryService influxQueryService, LogRepresentationAssembler assembler) {
        this.influxQueryService = influxQueryService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<LogRepresentationModel> all(
            @RequestParam(defaultValue = "start: 0") String range,
            @RequestParam(required = false) String hubId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String logType,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String aggregateWindow) {
        String fluxQuery = influxQueryService.produceQuery(bucket, range, measurement, field, hubId, deviceId, logType, sort, limit, aggregateWindow);
        List<InfluxDeviceLogPojo> queryResult = influxQueryService.query(fluxQuery, InfluxDeviceLogPojo.class);

        return assembler.toCollectionModel(queryResult);
    }
    @GetMapping("/{timestamp}")
    public LogRepresentationModel one(@PathVariable Instant timestamp) {
        String fluxQuery = influxQueryService.produceQuery(bucket, "start: " + timestamp, measurement, field, "", "", "","", "n: 1", "");
        List<InfluxDeviceLogPojo> queryResult = influxQueryService.query(fluxQuery, InfluxDeviceLogPojo.class);

        if (queryResult.isEmpty())
            throw new InfluxRecordNotFoundException(timestamp);

        return assembler.toModel(queryResult.get(0));
    }
}

