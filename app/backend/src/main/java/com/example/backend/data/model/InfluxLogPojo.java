package com.example.backend.data.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "log")
public class InfluxLogPojo extends InfluxBasePojo<String> {
    @Column(tag = true)
    private String logType;

    public InfluxLogPojo(Instant timestamp, String hubId, String deviceId, String value, String logType) {
        super(timestamp, hubId, deviceId, value);
        this.logType = logType;
    }

    @Override
    public String toString() {
        return "InfluxLogPojo{" +
                "logType='" + logType + '\'' +
                '}';
    }
}
