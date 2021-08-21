package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.DataRepresentationAssembler;
import com.example.backend.data.controllers.exceptions.InfluxRecordNotFoundException;
import com.example.backend.data.controllers.representation.models.DataRepresentationModel;
import com.example.backend.data.model.InfluxDeviceDataPojo;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("data")
public class DataController {
    private final InfluxQueryService influxQueryService;
    private final DataRepresentationAssembler assembler;
    private final String measurement = "deviceData";
    private final String field = "value";


    public DataController(InfluxQueryService influxQueryService, DataRepresentationAssembler assembler) {
        this.influxQueryService = influxQueryService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<DataRepresentationModel> all(
            @RequestParam(defaultValue = "data") String bucket,
            @RequestParam(defaultValue = "start: 0") String range,
            @RequestParam(required = false) String hubId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String measurementType,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String aggregateWindow) {
        String fluxQuery = influxQueryService.produceQuery(bucket, range, measurement, field, hubId, deviceId, measurementType, sort, limit, aggregateWindow);
        List<InfluxDeviceDataPojo> result = influxQueryService.query(fluxQuery, InfluxDeviceDataPojo.class);

        return assembler.toCollectionModelConsideringBucket(result, bucket);
    }

    @GetMapping("/{timestamp}")
    public DataRepresentationModel one(@PathVariable Instant timestamp, @RequestParam(defaultValue = "data") String bucket) {
        String fluxQuery = influxQueryService.produceQuery(bucket, "start: " + timestamp, measurement, field, "", "", "", "", "n: 1", "");
        List<InfluxDeviceDataPojo> queryResult = influxQueryService.query(fluxQuery, InfluxDeviceDataPojo.class);

        if (queryResult.isEmpty())
            throw new InfluxRecordNotFoundException(timestamp);

        return assembler.toModelConsideringBucket(queryResult.get(0), bucket);
    }
}
