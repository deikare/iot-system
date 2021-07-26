package com.example.backend.device.manager;

import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initializeHubs(HubRepository hubRepository) {
        return args -> {
            hubRepository.deleteAll();
            for (int i = 0; i < 100; i++)
                logger.info("Preloading: " + hubRepository.save(new Hub("H" + (i / 10))));
        };
    }
}
