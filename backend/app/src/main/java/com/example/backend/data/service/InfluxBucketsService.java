package com.example.backend.data.service;

import com.example.backend.data.controllers.exceptions.BucketNotFoundException;
import com.example.backend.data.model.mappers.InfluxBucketWithTagsPojo;
import com.example.backend.data.model.mappers.InfluxStringRecordPojo;
import com.example.backend.data.model.mappers.InfluxTagKey;
import com.example.backend.data.model.mappers.InfluxTagValues;
import com.example.backend.utilities.builders.page.PageBuilder;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.Bucket;
import com.influxdb.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.*;

public class InfluxBucketsService {
    private final BucketsApi bucketsApi;
    private final QueryApi queryApi;

    private final Logger logger = LoggerFactory.getLogger(InfluxBucketsService.class);

    public InfluxBucketsService(BucketsApi bucketsApi, QueryApi queryApi) {
        this.bucketsApi = bucketsApi;
        this.queryApi = queryApi;
    }


    public Page<InfluxBucketWithTagsPojo> getAllBuckets(Pageable pageable) {
        String org = "my-org";
        List<Bucket> queryResult = bucketsApi.findBucketsByOrgName(org);

        List<InfluxBucketWithTagsPojo> bucketNames = queryResult.stream()
                .filter(bucket -> !Objects.equals(bucket.getName(), "_monitoring") && !Objects.equals(bucket.getName(), "_tasks"))
                .map(bucket -> getBucketWithTags(bucket.getName()))
                .toList();

        Page<InfluxBucketWithTagsPojo> result = PageBuilder.build(bucketNames, pageable);

        logger.info("Query buckets: " + result);

        return result;
    }

    public InfluxBucketWithTagsPojo getBucketWithTags(String bucketName) throws BucketNotFoundException {
        String tagsQuery = produceBucketTagsQuery(bucketName);

        List<String> tagNames;

        try {
            tagNames = query(tagsQuery, InfluxStringRecordPojo.class).stream().map(InfluxStringRecordPojo::getValue).toList();
        } catch (NotFoundException e) {
            logger.info("Exception happened: " + e.getMessage());
            throw new BucketNotFoundException(bucketName);
        }

        HashMap<InfluxTagKey, InfluxTagValues> tagMap = new HashMap<>();

        for (String tagName : tagNames) {
            String tagKeysQuery = produceBucketTagValuesQuery(bucketName, tagName);

            List<String> tagValues = query(tagKeysQuery, InfluxStringRecordPojo.class).stream().map(InfluxStringRecordPojo::getValue).toList();

            tagMap.put(new InfluxTagKey(tagName), new InfluxTagValues(tagValues));
        }

        return new InfluxBucketWithTagsPojo(bucketName, tagMap);
    }

    public <I> List<I> query(String flux, Class<I> classToMapRawRecords) throws NotFoundException {
        logger.info("Starting query: " + flux);
        List<I> result = queryApi.query(flux, classToMapRawRecords);

        logger.info("Query result: " + result);
        return result;
    }

    private String produceBucketTagsQuery(String bucketName) {
        return "import \"influxdata/influxdb/schema\"" +
                "\n\n" +
                "schema.tagKeys(bucket: \"" + bucketName + "\", start: -100y)" +
                " " +
                "|> filter(fn: (r) => r._value != \"_start\" and r._value != \"_stop\" and r._value != \"_field\" and r._value != \"_measurement\")" +
                " " +
                "|> keep(columns: [\"_value\"])";
    }

    private String produceBucketTagValuesQuery(String bucketName, String tagName) {
        return "import \"influxdata/influxdb/schema\"" +
                "\n\n" +
                "schema.tagValues(bucket: \"" + bucketName + "\", tag: \"" + tagName + "\", start: -100y)";
    }
}
