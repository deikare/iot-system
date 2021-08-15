package com.example.backend.data.config;

import com.example.backend.data.service.InfluxBucketsService;
import com.example.backend.loggers.abstracts.ConfigLogger;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBBucketAPIConfig {
    private final InfluxDBClient influxDBClient;

    private final Logger logger = LoggerFactory.getLogger(InfluxDBBucketAPIConfig.class);


    public InfluxDBBucketAPIConfig(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Bean
    public InfluxBucketsService influxBucketsService() {
        InfluxBucketsService result = new InfluxBucketsService(bucketsApi());
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }

    @Bean
    public BucketsApi bucketsApi() {
        BucketsApi result = influxDBClient.getBucketsApi();
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }
}
