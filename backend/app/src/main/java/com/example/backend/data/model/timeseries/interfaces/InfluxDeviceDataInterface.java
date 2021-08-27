package com.example.backend.data.model.timeseries.interfaces;

import java.util.HashMap;

public interface InfluxDeviceDataInterface<V> extends InfluxDeviceLogInterface<V> {
    String getType();
    HashMap<String, String> getTags();
}
