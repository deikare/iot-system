package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.InfluxDataPojo;
import org.springframework.hateoas.RepresentationModel;

public class DataRepresentationModel extends RepresentationModel<DataRepresentationModel> {
    private final InfluxDataPojo influxDataPojo;

    public DataRepresentationModel(InfluxDataPojo influxDataPojo) {
        this.influxDataPojo = influxDataPojo;
    }

    public InfluxDataPojo getInfluxDataPojo() {
        return influxDataPojo;
    }
}
