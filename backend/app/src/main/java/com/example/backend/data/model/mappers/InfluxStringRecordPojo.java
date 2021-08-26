package com.example.backend.data.model.mappers;

import com.influxdb.annotations.Column;

public class InfluxStringRecordPojo {
    @Column
    private String value;

    public InfluxStringRecordPojo(String value) {
        this.value = value;
    }

    public InfluxStringRecordPojo() {
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InfluxStringRecordPojo{" +
                "value='" + value + '\'' +
                '}';
    }
}
