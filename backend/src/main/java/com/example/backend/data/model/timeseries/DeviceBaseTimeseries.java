package com.example.backend.data.model.timeseries;

import java.util.ArrayList;
import java.util.List;

public class DeviceBaseTimeseries<V> {
    private final List<Point<V>> points;

    public DeviceBaseTimeseries() {
        points = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DeviceBaseTimeseries{" +
                "points=" + points +
                '}';
    }

    public void addPoint(Point<V> newPoint) {
        points.add(newPoint);
    }

    public List<Point<V>> getPoints() {
        return points;
    }
}
