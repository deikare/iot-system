package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.InfluxDeviceLogPojo;
import org.springframework.hateoas.RepresentationModel;

public class LogRepresentationModel extends RepresentationModel<LogRepresentationModel> {
    private final InfluxDeviceLogPojo influxLogPojo;

    public LogRepresentationModel(InfluxDeviceLogPojo influxLogPojo) {
        this.influxLogPojo = influxLogPojo;
    }

    public InfluxDeviceLogPojo getInfluxLogPojo() {
        return influxLogPojo;
    }
}
