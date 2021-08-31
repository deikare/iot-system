package model;

import com.influxdb.annotations.Column;

import java.time.Instant;

public class InfluxDeviceBasePojo extends InfluxBasePojo {
    @Column(tag = true)
    protected String deviceId;

    public InfluxDeviceBasePojo(Instant time, String hubId, String deviceId, String bucketName) {
        super(time, hubId, bucketName);
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
        return "InfluxDeviceBasePojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
