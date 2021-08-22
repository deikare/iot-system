package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.DataTimeseriesListRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.DataTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceDataPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("data")
public class DataController {
    private final InfluxQueryService influxQueryService;
    private final DataTimeseriesListRepresentationAssembler assembler;
    private final String measurement = "deviceData";
    private final String field = "value";


    public DataController(InfluxQueryService influxQueryService, DataTimeseriesListRepresentationAssembler assembler) {
        this.influxQueryService = influxQueryService;
        this.assembler = assembler;
    }

    @GetMapping
    public DataTimeseriesListRepresentationModel all(
            @RequestParam(defaultValue = "data") String bucket,
            @RequestParam(defaultValue = "start: 0") String range,
            @RequestParam(required = false) String hubId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String measurementType,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String limit,
            @RequestParam(required = false) String aggregateWindow) {
        String fluxQuery = influxQueryService.produceQuery(bucket, range, measurement, field, hubId, deviceId, measurementType, sort, limit, aggregateWindow);
        DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> result = influxQueryService.queryWithResultMappedToTimeseriesList(fluxQuery, InfluxDeviceDataPojo.class);

        return assembler.toModelConsideringBucket(result, bucket);
    }
}
