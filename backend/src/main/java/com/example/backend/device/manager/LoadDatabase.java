package com.example.backend.device.manager;

import com.example.backend.data.model.InfluxDataPojo;
import com.example.backend.data.model.InfluxLogPojo;
import com.example.backend.data.service.InfluxQueryService;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.*;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableJpaAuditing
public class LoadDatabase {
    Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Bean
    CommandLineRunner initializeHubs(
            MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubServiceImplementation,
            MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceServiceImplementation,
            MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalServiceImplementation,
            DependentServiceImplementation<ControlSignalResponse, ControlSignal, Long, Long, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlResponseServiceImplementation,
            InfluxQueryService influxQueryService) {
        return args -> {
            hubServiceImplementation.deleteAllObjects();
            deviceServiceImplementation.deleteAllObjects();
            int amountOfHubs = 10;
            for (int i = 0; i < amountOfHubs; i++) {
                Hub hub = new Hub("H" + i);
                logger.info("Preloading hubs: " + hubServiceImplementation.addObject(hub));

                int amountOfDevices = ThreadLocalRandom.current().nextInt(1, 20);
                for (int j = 0; j < amountOfDevices; j++) {
                    Device device = new Device("D" + j, hub, DeviceType.BOTH);
                    logger.info("Preloading devices: " + deviceServiceImplementation.addDependentAndBindItToMaster(device, hub));

                    int amountOfControls = ThreadLocalRandom.current().nextInt(1, 10);
                    for (int k = 0; k < amountOfControls; k++) {
                        ControlSignal controlSignal = new ControlSignal("Cs" + j, randomString(4), device);
                        logger.info("Preloading controlSignals: " + controlSignalServiceImplementation.addDependentAndBindItToMaster(controlSignal, device));

                        int amountOfResponses = ThreadLocalRandom.current().nextInt(1, 5);
                        for(int l = 0; l < amountOfResponses; l++) {
                            ControlSignalResponse controlSignalResponse = new ControlSignalResponse("Csr" + l, randomString(4), controlSignal);
                            logger.info("Preloading responses: " + controlResponseServiceImplementation.addDependentAndBindItToMaster(controlSignalResponse, controlSignal));
                        }
                    }
                }
                logger.info("Hub after preloading devices: " + hub);
            }

            String flux = "from(bucket:\"data\") |> range(start: 0)";
            List<InfluxDataPojo> influxDataPojos = influxQueryService.query(flux, InfluxDataPojo.class);
            for (var dataPojo : influxDataPojos) {
                logger.info("dataPojo: " + dataPojo);
            }

            String flux2 = "from(bucket:\"logs\") |> range(start: 0)";
            List<InfluxLogPojo> influxLogPojos = influxQueryService.query(flux2, InfluxLogPojo.class);
            for (var logPojo : influxLogPojos) {
                logger.info("logPojo: " + logPojo);
            }
        };
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
