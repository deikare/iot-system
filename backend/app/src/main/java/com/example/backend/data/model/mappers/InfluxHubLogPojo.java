package com.example.backend.data.model.mappers;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "hubLog")
public class InfluxHubLogPojo extends InfluxBasePojo {
    @Column
    private InfluxHubStatusValue value;

    @Column(tag = true)
    private String name;

    public InfluxHubLogPojo(Instant time, String hubId, InfluxHubStatusValue value, String name) {
        super(time, hubId);
        this.value = value;
        this.name = name;
    }

    public InfluxHubLogPojo() {

    }

    public InfluxHubStatusValue getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "InfluxHubLogPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}