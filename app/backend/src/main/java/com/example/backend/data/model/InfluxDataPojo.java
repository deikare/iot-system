package com.example.backend.data.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

@Measurement(name = "data")
public class InfluxDataPojo extends InfluxBasePojo<Double> {
    @Column(tag = true)
    private String measurementType;

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String toString() {
        return "InfluxDataPojo{" +
                "measurementType='" + measurementType + '\'' +
                super.toString() +
                '}';
    }
}
