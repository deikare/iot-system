package com.example.backend.data.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "data")
public class InfluxDataPojo extends InfluxBasePojo<Double> {
    @Column(tag = true)
    private String measurementType;

    public InfluxDataPojo(Instant timestamp, String hubId, String deviceId, String measurementType, Double value) {
        super(timestamp, hubId, deviceId, value);
        this.measurementType = measurementType;
    }

    // default constructor for query result mapping
    public InfluxDataPojo() {
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public String toString() {
        return "InfluxDataPojo{" +
                "measurementType='" + measurementType + '\'' +
                super.toString() +
                '}';
    }
}
