package com.example.backend.data.model.mappers;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxBasePojo {
    @Column(timestamp = true)
    protected Instant time;

    @Column(tag = true)
    protected String hubId;

    public InfluxBasePojo(Instant time, String hubId) {
        this.time = time;
        this.hubId = hubId;
    }

    public InfluxBasePojo() {
    }

    public Instant getTime() {
        return time;
    }

    public String getHubId() {
        return hubId;
    }
}

