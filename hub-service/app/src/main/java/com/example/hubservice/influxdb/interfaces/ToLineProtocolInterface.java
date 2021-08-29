package com.example.hubservice.influxdb.interfaces;

import java.util.HashMap;

public interface ToLineProtocolInterface {
    String getBucket();
    String getMeasurement();
    Long getTimestamp();
    String getFieldKey();
    boolean isFieldValueString();
    String getFieldValueAsString();
    HashMap<String, String> getTagSet();
}
