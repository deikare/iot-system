package com.example.backend.data.config;

import com.example.backend.data.service.InfluxQueryService;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO perhaps useless
@Configuration
public class FluxQueryConfig {
/*    private final InfluxDBClient influxDBClient;

    public FluxQueryConfig(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    private final Logger logger = LoggerFactory.getLogger(FluxQueryConfig.class);

    @Bean
    public InfluxQueryService influxQueryService() {
        InfluxQueryService result = new InfluxQueryService(queryApi());
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }

    @Bean
    public QueryApi queryApi() {
        QueryApi result = influxDBClient.getQueryApi();
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }*/
}