package com.example.backend.data.service;

import com.influxdb.client.BucketsApi;
import com.influxdb.client.domain.Bucket;

import java.util.List;

public class InfluxBucketsService {
    private final BucketsApi bucketsApi;

    public InfluxBucketsService(BucketsApi bucketsApi) {
        this.bucketsApi = bucketsApi;
    }

    public List<Bucket> getAllBuckets() {
        return bucketsApi.findBucketsByOrgName("my-org");
    }
}
