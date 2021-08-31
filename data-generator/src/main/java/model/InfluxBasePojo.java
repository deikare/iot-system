package model;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxBasePojo {
    @Column(timestamp = true)
    protected Instant time;

    @Column(tag = true)
    protected String hubId;

    @Column(tag = true)
    protected String bucketName;

    public InfluxBasePojo(Instant time, String hubId, String bucketName) {
        this.time = time;
        this.hubId = hubId;
        this.bucketName = bucketName;
    }

    public InfluxBasePojo() {
    }

    public String getBucketName() {
        return bucketName;
    }

    public Instant getTime() {
        return time;
    }

    public String getHubId() {
        return hubId;
    }
}
