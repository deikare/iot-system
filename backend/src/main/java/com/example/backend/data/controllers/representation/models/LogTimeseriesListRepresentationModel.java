package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import org.springframework.hateoas.RepresentationModel;

public class LogTimeseriesListRepresentationModel extends RepresentationModel<LogTimeseriesListRepresentationModel> {
    private final DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> timeseriesList;

    public LogTimeseriesListRepresentationModel(DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> timeseriesList) {
        this.timeseriesList = timeseriesList;
    }

    public DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> getTimeseriesList() {
        return timeseriesList;
    }
}
