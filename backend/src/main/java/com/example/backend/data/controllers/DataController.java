package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.DataTimeseriesListRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.DataTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceDataPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import com.example.backend.data.service.InfluxQueryService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

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
            @RequestParam(required = false) Instant start,
            @RequestParam(required = false) Instant stop,
            @RequestParam(defaultValue = "true") boolean desc,
            @RequestParam(required = false) Long limit,
            @RequestParam(required = false) List<String> hubIds,
            @RequestParam(required = false) List<String> deviceIds,
            @RequestParam(required = false) List<String> measurementTypes) {
        String fluxQuery = influxQueryService.produceQuery(bucket, measurement, field, start, stop, desc, hubIds, deviceIds, measurementTypes);
        DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> result = influxQueryService.queryWithResultMappedToTimeseriesList(fluxQuery, limit, InfluxDeviceDataPojo.class);

        return assembler.toModelConsideringQueryParams(result, bucket, desc, limit, hubIds, deviceIds, measurementTypes);
    }
}
