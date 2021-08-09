package com.example.backend.data.model;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxBasePojo<V> {
    @Column(timestamp = true)
    private Instant timestamp;

    @Column(tag = true)
    private String hubId;

    @Column(tag = true)
    private String deviceId;

    @Column
    private V value;

    public InfluxBasePojo(Instant timestamp, String hubId, String deviceId, V value) {
        this.timestamp = timestamp;
        this.hubId = hubId;
        this.deviceId = deviceId;
        this.value = value;
    }

    // default constructor for query result mapping
    public InfluxBasePojo() {
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getHubId() {
        return hubId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InfluxBasePojo{" +
                "timestamp=" + timestamp +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", value=" + value +
                '}';
    }
}
