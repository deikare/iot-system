package model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "data")
public class InfluxDataPojo extends InfluxBasePojo {
    @Column(tag = true)
    private String measurementType;

    @Column
    private double value;

    public InfluxDataPojo(Instant time, String hubId, String deviceId, double value, String measurementType) {
        super(time, hubId, deviceId);
        this.measurementType = measurementType;
        this.value = value;
    }

    // default constructor for query result mapping
    public InfluxDataPojo() {
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InfluxDataPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", measurementType='" + measurementType + '\'' +
                ", value=" + value +
                '}';
    }
}
