package com.example.backend.data.model;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxBasePojo {
    @Column(timestamp = true)
    protected Instant time;

    @Column(tag = true)
    protected String hubId;

    @Column(tag = true)
    protected String deviceId;

    @Column(tag = true)
    protected String type;

    public InfluxBasePojo(Instant time, String hubId, String deviceId, String type) {
        this.time = time;
        this.hubId = hubId;
        this.deviceId = deviceId;
        this.type = type;
    }

    // default constructor for query result mapping
    public InfluxBasePojo() {

    }

    public Instant getTime() {
        return time;
    }

    public String getHubId() {
        return hubId;
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
