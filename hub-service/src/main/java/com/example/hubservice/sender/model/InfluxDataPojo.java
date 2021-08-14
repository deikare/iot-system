package com.example.hubservice.sender.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Measurement(name = "data")
@RedisHash("data")
public class InfluxDataPojo extends InfluxBasePojo {
    @Column
    private double value;

    public InfluxDataPojo(Instant time, String hubId, String deviceId, double value, String measurementType) {
        super(time, hubId, deviceId, measurementType);
        this.value = value;
    }

    // default constructor for query result mapping
    public InfluxDataPojo() {
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
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}

