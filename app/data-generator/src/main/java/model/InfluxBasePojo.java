package model;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxBasePojo {
    @Column(timestamp = true)
    protected Instant time;

    @Column(tag = true)
    protected String hubId;

    @Column(tag = true)
    protected String deviceId;

    public InfluxBasePojo(Instant time, String hubId, String deviceId) {
        this.time = time;
        this.hubId = hubId;
        this.deviceId = deviceId;
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

    @Override
    public String toString() {
        return "InfluxBasePojo{" +
                "timestamp=" + time +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
