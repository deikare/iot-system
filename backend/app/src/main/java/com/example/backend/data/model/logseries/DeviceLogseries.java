package com.example.backend.data.model.logseries;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceBaseInterface;
import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceLogInterface;

import java.time.Instant;
import java.util.List;

public class DeviceLogseries<I extends InfluxDeviceLogInterface<String>> {
    private final List<I> logs;

    private final int numberOfPoints;

    private Instant start = Instant.now();
    private Instant end = Instant.ofEpochMilli(0);

    public DeviceLogseries(List<I> logs) {
        this.logs = logs;
        numberOfPoints = logs.size();

        start = logs.stream()
                .map(InfluxDeviceBaseInterface::getTime)
                .min(Instant::compareTo).orElseThrow();

        end = logs.stream()
                .map(InfluxDeviceBaseInterface::getTime)
                .max(Instant::compareTo).orElseThrow();
    }

    @Override
    public String toString() {
        return "DeviceLogseries{" +
                "logs=" + logs +
                ", numberOfPoints=" + numberOfPoints +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public List<I> getLogs() {
        return logs;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
