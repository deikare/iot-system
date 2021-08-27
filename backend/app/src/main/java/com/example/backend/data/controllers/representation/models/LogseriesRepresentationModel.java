package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.logseries.DeviceLogseries;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import org.springframework.hateoas.RepresentationModel;

public class LogseriesRepresentationModel extends RepresentationModel<LogseriesRepresentationModel> {
    private final DeviceLogseries<InfluxDeviceLogPojo> logseries;

    public LogseriesRepresentationModel(DeviceLogseries<InfluxDeviceLogPojo> logseries) {
        this.logseries = logseries;
    }

    public DeviceLogseries<InfluxDeviceLogPojo> getLogseries() {
        return logseries;
    }
}
