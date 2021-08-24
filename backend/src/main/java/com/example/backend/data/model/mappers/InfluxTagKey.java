package com.example.backend.data.model.mappers;

import java.util.Objects;

public class InfluxTagKey {
    private final String name;

    public InfluxTagKey(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InfluxTagKey{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfluxTagKey that = (InfluxTagKey) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}

