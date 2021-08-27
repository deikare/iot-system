package com.example.backend.data.model.timeseries.interfaces;

public interface InfluxDeviceLogInterface<V> extends InfluxDeviceBaseInterface {
    V getValueAsObject(); //we need value as Object - e.g. Double instead of double because of generic use
}
