package com.example.backend.data.model.timeseries;

import java.time.Instant;

public class Point<V> {
    private final Instant time;
    private final V value;

    public Point(Instant time, V value) {
        this.time = time;
        this.value = value;
    }

    public Instant getTime() {
        return time;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Point{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
