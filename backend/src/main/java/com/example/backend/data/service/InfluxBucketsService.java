package com.example.backend.data.service;

import com.example.backend.utilities.builders.page.PageBuilder;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.domain.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;

public class InfluxBucketsService {
    private final BucketsApi bucketsApi;

    private final Logger logger = LoggerFactory.getLogger(InfluxBucketsService.class);


    public InfluxBucketsService(BucketsApi bucketsApi) {
        this.bucketsApi = bucketsApi;
    }

    public Page<Bucket> getAllBuckets(Pageable pageable) {
        String org = "my-org";
        List<Bucket> queryResult = bucketsApi.findBucketsByOrgName(org);

        queryResult = queryResult.stream()
                .filter(bucket -> !Objects.equals(bucket.getName(), "_monitoring") && !Objects.equals(bucket.getName(), "_tasks"))
                .toList();

        Page<Bucket> result = PageBuilder.build(queryResult, pageable);

        logger.info("Query buckets: " + result);

        return result;
    }

}
