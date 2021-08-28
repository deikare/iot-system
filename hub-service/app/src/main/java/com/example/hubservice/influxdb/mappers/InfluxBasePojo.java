package com.example.hubservice.influxdb.mappers;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxBasePojo {
    @Column(timestamp = true)
    protected Instant time;

    @Column(tag = true)
    protected String hubId;

    @Column(tag = true)
    protected InfluxBucketName bucketName;

    public InfluxBasePojo(Instant time, String hubId, InfluxBucketName bucketName) {
        this.time = time;
        this.hubId = hubId;
        this.bucketName = bucketName;
    }

    public InfluxBasePojo() {
    }

    public InfluxBucketName getBucketName() {
        return bucketName;
    }

    public Instant getTime() {
        return time;
    }

    public String getHubId() {
        return hubId;
    }
}
