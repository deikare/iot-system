package com.example.backend.device.manager;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.old.DeviceServiceImplementation;
import com.example.backend.device.manager.service.implementation.old.HubServiceImplementation2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class LoadDatabase {
    Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initializeHubs(
            HubServiceImplementation2 hubServiceImplementation,
            DeviceServiceImplementation deviceServiceImplementation) {
        return args -> {
            hubServiceImplementation.deleteAllHubs();
            deviceServiceImplementation.deleteAllDevices();
            for (int i = 0; i < 100; i++) {
                Hub hub = new Hub("H" + (i / 10));
                logger.info("Preloading hubs: " + hubServiceImplementation.addHub(hub));

                int amountOfDevices = ThreadLocalRandom.current().nextInt(0, 10);
                for (int j = 0; j < amountOfDevices; j++) {
                    Device device = new Device("D" + j, hub, DeviceType.BOTH);
                    logger.info("Preloading devices: " + deviceServiceImplementation.addDeviceAndBindItToHub(device, hub));
                }
                logger.info("Hub after preloading devices: " + hub);
            }
        };
    }
}
