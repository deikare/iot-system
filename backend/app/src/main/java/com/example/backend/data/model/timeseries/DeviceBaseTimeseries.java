package com.example.backend.data.model.timeseries;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DeviceBaseTimeseries<V> {
    private final List<Point<V>> points;

    private int numberOfPoints;

    private Instant start = Instant.now();
    private Instant end = Instant.ofEpochMilli(0);

    public DeviceBaseTimeseries() {
        points = new ArrayList<>();
        numberOfPoints = 0;
    }


    @Override
    public String toString() {
        return "DeviceBaseTimeseries{" +
                "points=" + points +
                ", numberOfPoints=" + numberOfPoints +
                '}';
    }

    public void addPoint(Point<V> newPoint) {
        Instant recordTime = newPoint.getTime();

        if (end.compareTo(recordTime) < 0)
            end = recordTime;
        if (start.compareTo(recordTime) > 0)
            start = recordTime;

        points.add(newPoint);
        numberOfPoints++;
    }

    public List<Point<V>> getPoints() {
        return points;
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
