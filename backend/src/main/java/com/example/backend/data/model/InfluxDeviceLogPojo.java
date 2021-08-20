package com.example.backend.data.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "deviceLog")
public class InfluxDeviceLogPojo extends InfluxDeviceBasePojo {
    @Column
    private String value;

    public InfluxDeviceLogPojo(Instant time, String hubId, String deviceId, String value, String logType) {
        super(time, hubId, deviceId, logType);
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
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}