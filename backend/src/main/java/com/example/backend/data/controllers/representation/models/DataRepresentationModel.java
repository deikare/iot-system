package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.InfluxDeviceDataPojo;
import org.springframework.hateoas.RepresentationModel;

public class DataRepresentationModel extends RepresentationModel<DataRepresentationModel> {
    private final InfluxDeviceDataPojo influxDataPojo;

    public DataRepresentationModel(InfluxDeviceDataPojo influxDataPojo) {
        this.influxDataPojo = influxDataPojo;
    }

    public InfluxDeviceDataPojo getInfluxDataPojo() {
        return influxDataPojo;
    }
}
