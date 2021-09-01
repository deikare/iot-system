package com.example.hubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubServiceApplication.class, args);
    }

}
