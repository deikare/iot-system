package com.example.backend.device.config;

import com.example.backend.device.config.model.Hub;
import com.example.backend.device.config.repositories.HubRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    /*@Bean
    CommandLineRunner initializeHubs(HubRepository hubRepository) {
        return args -> {
            hubRepository.save(new Hub("Lol"));
        };
    }*/
}
