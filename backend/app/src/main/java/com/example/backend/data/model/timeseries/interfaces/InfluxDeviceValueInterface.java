package com.example.backend.data.model.timeseries.interfaces;

public interface InfluxDeviceValueInterface<V> extends InfluxDeviceBaseInterface {
    V getValueAsObject(); //we need value as Object - e.g. Double instead of double because of generic use
}
