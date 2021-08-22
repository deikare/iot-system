package com.example.backend.data.model.timeseries;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceValueInterface;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DeviceBaseTimeseriesList<V, I extends InfluxDeviceValueInterface<V>> {
    private final HashMap<TimeseriesTags<I>, DeviceBaseTimeseries<V>> timeseriesList;

    private Instant start = Instant.now();
    private Instant end = Instant.ofEpochMilli(0);

    private final int seriesAmount;
    private final int pointsAmount;

    public DeviceBaseTimeseriesList(List<I> recordsList) {
        timeseriesList = new HashMap<>();
        populateTimeseriesList(recordsList);

        seriesAmount = timeseriesList.size();
        pointsAmount = recordsList.size();
    }

    @Override
    public String toString() {
        return "DeviceBaseTimeseriesList{" +
                "timeseriesList=" + timeseriesList +
                '}';
    }

    public HashMap<TimeseriesTags<I>, DeviceBaseTimeseries<V>> getTimeseriesList() {
        return timeseriesList;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public int getSeriesAmount() {
        return seriesAmount;
    }

    public int getPointsAmount() {
        return pointsAmount;
    }

    private void populateTimeseriesList(List<I> recordsList) {
        for (I record : recordsList) {
            Instant recordTime = record.getTime();

            if (end.compareTo(recordTime) < 0)
                end = record.getTime();
            if (start.compareTo(recordTime) > 0)
                start = record.getTime();

            TimeseriesTags<I> tags = new TimeseriesTags<>(record);

            DeviceBaseTimeseries<V> timeseries = timeseriesList.get(tags);

            Point<V> newPoint = new Point<>(record.getTime(), record.getValueAsObject());

            if (timeseries == null)
                putNewTimeseries(tags, newPoint);
            else addNewPointToExistingTimeseries(timeseries, newPoint);
        }
    }

    private void putNewTimeseries(TimeseriesTags<I> tags, Point<V> newPoint) {
        DeviceBaseTimeseries<V> newTimeseries = new DeviceBaseTimeseries<>();
        newTimeseries.addPoint(newPoint);
        timeseriesList.put(tags, newTimeseries);
    }

    private void addNewPointToExistingTimeseries(DeviceBaseTimeseries<V> timeseries, Point<V> newPoint) {
        timeseries.addPoint(newPoint);
    }
}
