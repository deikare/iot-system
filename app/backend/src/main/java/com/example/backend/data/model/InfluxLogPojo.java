package com.example.backend.data.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

@Measurement(name = "log")
public class InfluxLogPojo extends InfluxBasePojo<String> {
    @Column(tag = true)
    private String logType;

    public String toString() {
        return "InfluxLogPojo{" +
                "logType='" + logType + '\'' +
                super.toString() +
                '}';
    }
}
