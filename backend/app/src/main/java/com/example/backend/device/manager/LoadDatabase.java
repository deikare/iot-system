package com.example.backend.device.manager;

import com.example.backend.data.model.mappers.InfluxHubStatusValue;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableJpaAuditing
public class LoadDatabase {
    Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    /*@Bean
    CommandLineRunner initializeHubs(
            MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubServiceImplementation,
            MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceServiceImplementation,
            DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalServiceImplementation) {
        return args -> {
            hubServiceImplementation.deleteAllObjects();
            deviceServiceImplementation.deleteAllObjects();
            int amountOfHubs = 10;
            for (int i = 0; i < amountOfHubs; i++) {
                Hub hub = new Hub(UUID.randomUUID().toString(), "H" + i, InfluxHubStatusValue.CREATED);
                logger.info("Preloading hubs: " + hubServiceImplementation.addObject(hub));

                int amountOfDevices = ThreadLocalRandom.current().nextInt(1, 20);
                for (int j = 0; j < amountOfDevices; j++) {
                    Device device = new Device("D" + j, hub, DeviceType.BOTH);
                    logger.info("Preloading devices: " + deviceServiceImplementation.addDependentAndBindItToMaster(device, hub));

                    int amountOfControls = ThreadLocalRandom.current().nextInt(1, 10);
                    for (int k = 0; k < amountOfControls; k++) {
                        ControlSignal controlSignal = new ControlSignal("Cs" + j, randomString(4), device);
                        logger.info("Preloading controlSignals: " + controlSignalServiceImplementation.addDependentAndBindItToMaster(controlSignal, device));
                    }
                }
                logger.info("Hub after preloading devices: " + hub);
            }
        };
    }*/

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
