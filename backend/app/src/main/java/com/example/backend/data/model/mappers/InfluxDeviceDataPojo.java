package com.example.backend.data.model.mappers;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceDataInterface;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;
import java.util.HashMap;

@Measurement(name = "deviceData")
public class InfluxDeviceDataPojo extends InfluxDeviceBasePojo implements InfluxDeviceDataInterface<Double> {
    @Column
    private double value;

    @Column(tag = true)
    private String type;

    public InfluxDeviceDataPojo(Instant time, String hubId, String deviceId, double value, String measurementType) {
        super(time, hubId, deviceId);
        this.value = value;
        this.type = measurementType;
    }

    // default constructor for query result mapping
    public InfluxDeviceDataPojo() {
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

    @Override
    public Double getValueAsObject() {
        return value;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public HashMap<String, String> getTags() {
        HashMap<String, String> result = new HashMap<>();

        result.put("hubId", this.hubId);
        result.put("deviceId", this.deviceId);
        result.put("type", this.type);

        return result;
    }
}

