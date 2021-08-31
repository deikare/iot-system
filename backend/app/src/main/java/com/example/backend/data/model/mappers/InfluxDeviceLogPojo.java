package com.example.backend.data.model.mappers;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceLogInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "deviceLog")
public class InfluxDeviceLogPojo extends InfluxDeviceBasePojo  implements InfluxDeviceLogInterface<String> {
    @Column
    private String value;

    public InfluxDeviceLogPojo(Instant time, String hubId, String deviceId, String value) {
        super(time, hubId, deviceId);
        this.value = value;
    }

    // default constructor for query result mapping
    public InfluxDeviceLogPojo() {
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InfluxLogPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @JsonIgnore
    @Override
    public String getValueAsObject() {
        return getValue();
    }
}