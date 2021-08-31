package model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "deviceData")
public class InfluxDeviceDataPojo extends InfluxDeviceBasePojo {
    @Column
    private double value;

    @Column(tag = true)
    private String type;

    public InfluxDeviceDataPojo(Instant time, String hubId, String deviceId, double value, String bucketName, String type) {
        super(time, hubId, deviceId, bucketName);
        this.value = value;

        this.type = type;
    }

    // default constructor for query result mapping
    public InfluxDeviceDataPojo() {
    }

    public double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "InfluxDeviceDataPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", value=" + value +
                ", type='" + type + '\'' +
                '}';
    }
}

