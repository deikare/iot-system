package com.example.backend.data.service;

import com.example.backend.utilities.page.builders.PageBuilder;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.FindOptions;
import com.influxdb.client.domain.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

public class InfluxBucketsService {
    private final BucketsApi bucketsApi;

    private final Logger logger = LoggerFactory.getLogger(InfluxBucketsService.class);


    public InfluxBucketsService(BucketsApi bucketsApi) {
        this.bucketsApi = bucketsApi;
    }

    public Page<Bucket> getAllBuckets(Pageable pageable) {
        List<Bucket> queryResult = bucketsApi.findBucketsByOrgName("my-org");
        Page<Bucket> result = PageBuilder.build(queryResult, pageable);

        logger.info("Query buckets: " + result);

        return result;
    }

}
