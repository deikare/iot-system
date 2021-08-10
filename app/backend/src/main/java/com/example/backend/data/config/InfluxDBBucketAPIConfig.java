package com.example.backend.data.config;

import com.example.backend.data.service.InfluxBucketsService;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBBucketAPIConfig {
    private final InfluxDBClient influxDBClient;

    public InfluxDBBucketAPIConfig(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Bean
    public InfluxBucketsService influxBucketsService() {
        return new InfluxBucketsService(bucketsApi());
    }

    @Bean
    public BucketsApi bucketsApi() {
        return influxDBClient.getBucketsApi();
    }
}
