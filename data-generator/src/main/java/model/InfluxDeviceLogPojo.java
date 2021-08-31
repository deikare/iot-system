package model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "deviceLog")
public class InfluxDeviceLogPojo extends InfluxDeviceBasePojo {
    @Column
    private String value;

    public InfluxDeviceLogPojo(Instant time, String hubId, String deviceId, String bucketName, String value) {
        super(time, hubId, deviceId, bucketName);
        this.value = value;
    }

    // default constructor for query result mapping
    public InfluxDeviceLogPojo() {
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InfluxDeviceLogPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}