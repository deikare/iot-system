package com.example.backend.data.config;

import com.example.backend.data.service.InfluxQueryService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBQueryConfig {
    private final InfluxDBClient influxDBClient;

    public InfluxDBQueryConfig(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Bean
    public InfluxQueryService influxQueryService() {
        return new InfluxQueryService(queryApi());
    }

    @Bean
    public QueryApi queryApi() {
        return influxDBClient.getQueryApi();
    }
}
