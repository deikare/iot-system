package com.example.backend.data.model.timeseries;

import com.example.backend.data.model.timeseries.interfaces.InfluxDeviceBaseInterface;

import java.util.Objects;

public class TimeseriesTags<I extends InfluxDeviceBaseInterface> {
    private final String hubId;
    private final String deviceId;
    private final String type;

    public TimeseriesTags(I pojo) {
        hubId = pojo.getHubId();
        deviceId = pojo.getDeviceId();
        type = pojo.getType();
    }

    public String getHubId() {
        return hubId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TimeseriesTags{" +
                "hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeseriesTags<?> that = (TimeseriesTags<?>) o;
        return Objects.equals(hubId, that.hubId) && Objects.equals(deviceId, that.deviceId) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hubId, deviceId, type);
    }
}
