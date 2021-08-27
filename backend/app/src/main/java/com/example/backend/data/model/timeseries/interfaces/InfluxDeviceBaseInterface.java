package com.example.backend.data.model.timeseries.interfaces;

import java.time.Instant;

public interface InfluxDeviceBaseInterface {
    Instant getTime();
    String getHubId();
    String getDeviceId();
}
