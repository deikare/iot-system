package com.example.backend.data.model.mappers;

import java.util.List;
import java.util.Objects;

public class InfluxTagValues {
    private final List<String> values;

    public InfluxTagValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "InfluxTagValues{" +
                "values=" + values +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfluxTagValues that = (InfluxTagValues) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    public List<String> getValues() {
        return values;
    }
}
