package com.example.hubservice.management.hub.service.implementation.initializer;

import com.example.hubservice.management.hub.exceptions.NoHubsFoundException;
import com.example.hubservice.management.hub.model.Hub;
import com.example.hubservice.management.hub.service.implementation.hub.configuration.HubConfigurationService;
import com.example.hubservice.management.hub.service.implementation.telegraf.TelegrafSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentInitializer {
    private final Logger logger = LoggerFactory.getLogger(AgentInitializer.class);
    @Bean
    CommandLineRunner initializeStack(HubConfigurationService hubConfigurationService, TelegrafSender telegrafSender) {
        return args -> {
            Hub hub;

            try {
                hub = hubConfigurationService.getHub();
            } catch (NoHubsFoundException e) {
                hub = hubConfigurationService.initializeHub();
            }

            telegrafSender.sendHubLogToTelegraf(hub);
        };


    }
}
