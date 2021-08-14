package com.example.hubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableRedisRepositories
public class HubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubServiceApplication.class, args);
    }

}
