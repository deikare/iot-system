package com.example.backend.data.model.mappers;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxDeviceBasePojo extends InfluxBasePojo {
    @Column(tag = true)
    protected String deviceId;

    public InfluxDeviceBasePojo(Instant time, String hubId, String deviceId) {
        super(time, hubId);
        this.deviceId = deviceId;
    }

    // default constructor for query result mapping
    public InfluxDeviceBasePojo() {

    }

    public String getDeviceId() {
        return deviceId;
    }


    @Override
    public String toString() {
        return "InfluxBasePojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
