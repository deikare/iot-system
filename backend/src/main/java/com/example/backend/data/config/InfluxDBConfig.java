package com.example.backend.data.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {
    private final String url = "http://localhost:8086";
    private final char[] token = "my-token".toCharArray();
    private final String org = "my-org";

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token, org);
    }
}
