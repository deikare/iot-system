package com.example.backend.data.model.timeseries;

import java.time.Instant;

public class Point<V> {
    private final Instant timestamp;
    private final V value;

    public Point(Instant timestamp, V value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Point{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
