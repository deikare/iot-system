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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
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
