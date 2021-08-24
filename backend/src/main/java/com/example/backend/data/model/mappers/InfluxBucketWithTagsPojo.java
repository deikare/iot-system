package com.example.backend.data.model.mappers;

import java.util.HashMap;

public class InfluxBucketWithTagsPojo {
    private final String name;
    private final HashMap<InfluxTagKey, InfluxTagValues> tagMap;

    public InfluxBucketWithTagsPojo(String name, HashMap<InfluxTagKey, InfluxTagValues> tagMap) {
        this.name = name;
        this.tagMap = tagMap;
    }

    public String getName() {
        return name;
    }

    public HashMap<InfluxTagKey, InfluxTagValues> getTagMap() {
        return tagMap;
    }
}
