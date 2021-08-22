package com.example.backend.data.model.timeseries;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceValueInterface;

import java.util.HashMap;
import java.util.List;

public class DeviceBaseTimeseriesList<V, I extends InfluxDeviceValueInterface<V>> {
    private final HashMap<TimeseriesTags<I>, DeviceBaseTimeseries<V>> timeseriesList;

    public DeviceBaseTimeseriesList(List<I> recordsList) {
        timeseriesList = new HashMap<>();
        populateTimeseriesList(recordsList);
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

    private void populateTimeseriesList(List<I> recordsList) {
        for (I record : recordsList) {
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
