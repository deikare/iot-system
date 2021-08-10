package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.InfluxLogPojo;
import org.springframework.hateoas.RepresentationModel;

public class LogRepresentationModel extends RepresentationModel<LogRepresentationModel> {
    private final InfluxLogPojo influxLogPojo;

    public LogRepresentationModel(InfluxLogPojo influxLogPojo) {
        this.influxLogPojo = influxLogPojo;
    }

    public InfluxLogPojo getInfluxLogPojo() {
        return influxLogPojo;
    }
}
