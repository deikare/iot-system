package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.mappers.InfluxDeviceDataPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import org.springframework.hateoas.RepresentationModel;

public class DataTimeseriesListRepresentationModel extends RepresentationModel<DataTimeseriesListRepresentationModel> {
    private final DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> timeseriesList;

    public DataTimeseriesListRepresentationModel(DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> timeseriesList) {
        this.timeseriesList = timeseriesList;
    }

    public DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> getTimeseriesList() {
        return timeseriesList;
    }
}