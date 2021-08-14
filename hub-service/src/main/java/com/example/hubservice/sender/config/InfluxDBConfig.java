package com.example.hubservice.sender.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {
    private final String url = "http://localhost:8086";
    private final char[] token = "my-token".toCharArray();
    private final String org = "my-org";
    private final String dataBucket = "data";
    private final String logsBucket = "logs";

    @Bean
    public WriteApi dataWriteApi() {
        return dataInfluxDBClient().getWriteApi();
    }

    @Bean
    public WriteApi logsWriteApi() {
        return logsInfluxDBClient().getWriteApi();
    }

    @Bean
    public InfluxDBClient dataInfluxDBClient() {
        return InfluxDBClientFactory.create(url, token, org, dataBucket);
    }

    @Bean
    public InfluxDBClient logsInfluxDBClient() {
        return InfluxDBClientFactory.create(url, token, org, logsBucket);
    }


}
