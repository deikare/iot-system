package com.example.backend.data.service.config;

import com.example.backend.data.service.InfluxBucketsService;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBBucketAPIConfig {
    private final InfluxDBClient influxDBClient;
    private final QueryApi queryApi;

    private final Logger logger = LoggerFactory.getLogger(InfluxDBBucketAPIConfig.class);


    public InfluxDBBucketAPIConfig(InfluxDBClient influxDBClient, QueryApi queryApi) {
        this.influxDBClient = influxDBClient;
        this.queryApi = queryApi;
    }

    @Bean
    public InfluxBucketsService influxBucketsService() {
        InfluxBucketsService result = new InfluxBucketsService(bucketsApi(), queryApi);
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
