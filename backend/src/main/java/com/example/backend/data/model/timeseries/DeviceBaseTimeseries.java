package com.example.backend.data.model.timeseries;

import java.util.ArrayList;
import java.util.List;

public class DeviceBaseTimeseries<V> {
    private final List<Point<V>> points;

    private int numberOfPoints;

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
        points.add(newPoint);
        numberOfPoints++;
    }

    public List<Point<V>> getPoints() {
        return points;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }
}
