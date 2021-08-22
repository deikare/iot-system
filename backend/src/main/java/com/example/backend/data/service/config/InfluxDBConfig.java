package com.example.backend.data.service.config;

import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {
    private final String url = "http://localhost:8086";
    private final char[] token = "my-token".toCharArray();
    private final String org = "my-org";

    private final Logger logger = LoggerFactory.getLogger(InfluxDBConfig.class);

    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient result = InfluxDBClientFactory.create(url, token, org);
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }
}
