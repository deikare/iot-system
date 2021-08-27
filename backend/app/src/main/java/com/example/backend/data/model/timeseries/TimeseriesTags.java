package com.example.backend.data.model.timeseries;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceBaseInterface;
import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceDataInterface;

import java.util.HashMap;
import java.util.Objects;

public class TimeseriesTags<V, I extends InfluxDeviceDataInterface<V>> {
    private final HashMap<String, String> tags;

    public TimeseriesTags(I pojo) {
        tags = pojo.getTags();
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "TimeseriesTags{" +
                "tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeseriesTags<?, ?> that = (TimeseriesTags<?, ?>) o;
        return tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags);
    }
}
