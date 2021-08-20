package com.example.backend.device.manager.service.implementation.discovery;

import com.influxdb.client.flux.FluxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HubDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(HubDiscoveryService.class);

    @Scheduled(fixedDelay = 10000)
    void discoverServices() {
    }

}
