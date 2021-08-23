package com.example.backend.data.service.config;

import com.example.backend.data.service.InfluxQueryService;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.QueryReactiveApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBQueryConfig {
    private final InfluxDBClient influxDBClient;
    private final InfluxDBClientReactive influxDBClientReactive;

    public InfluxDBQueryConfig(InfluxDBClient influxDBClient, InfluxDBClientReactive influxDBClientReactive) {
        this.influxDBClient = influxDBClient;
        this.influxDBClientReactive = influxDBClientReactive;
    }

    private final Logger logger = LoggerFactory.getLogger(InfluxDBQueryConfig.class);

    @Bean
    public InfluxQueryService influxQueryService() {
        InfluxQueryService result = new InfluxQueryService(queryApi(), queryReactiveApi());
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }

    @Bean
    public QueryApi queryApi() {
        QueryApi result = influxDBClient.getQueryApi();
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }

    @Bean
    public QueryReactiveApi queryReactiveApi() {
        QueryReactiveApi result = influxDBClientReactive.getQueryReactiveApi();
        ConfigLogger.produceConfigBeanCreationLog(logger, result);
        return result;
    }
}
