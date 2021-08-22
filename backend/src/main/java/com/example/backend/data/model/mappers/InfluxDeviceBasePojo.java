package com.example.backend.data.model.mappers;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxDeviceBasePojo extends InfluxBasePojo {
    @Column(tag = true)
    protected String deviceId;

    @Column(tag = true)
    protected String type;

    public InfluxDeviceBasePojo(Instant time, String hubId, String deviceId, String type) {
        super(time, hubId);
        this.deviceId = deviceId;
        this.type = type;
    }

    // default constructor for query result mapping
    public InfluxDeviceBasePojo() {

    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "InfluxBasePojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
