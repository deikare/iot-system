package com.example.backend.device.manager;

import com.example.backend.data.model.InfluxDataPojo;
import com.example.backend.data.model.InfluxLogPojo;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.influxdb.client.QueryApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableJpaAuditing
public class LoadDatabase {
    Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initializeHubs(
            MasterServiceImplementation<Hub, Device, HubNotFoundException> hubServiceImplementation,
            MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> deviceServiceImplementation,
            QueryApi queryApi) {
        return args -> {
            hubServiceImplementation.deleteAllObjects();
            deviceServiceImplementation.deleteAllObjects();
            for (int i = 0; i < 100; i++) {
                Hub hub = new Hub("H" + (i / 10));
                logger.info("Preloading hubs: " + hubServiceImplementation.addObject(hub));

                int amountOfDevices = ThreadLocalRandom.current().nextInt(0, 10);
                for (int j = 0; j < amountOfDevices; j++) {
                    Device device = new Device("D" + j, hub, DeviceType.BOTH);
                    logger.info("Preloading devices: " + deviceServiceImplementation.addDependentAndBindItToMaster(device, hub));
                }
                logger.info("Hub after preloading devices: " + hub);
            }

            String flux = "from(bucket:\"data\") |> range(start: 0)";
            List<InfluxDataPojo> influxDataPojos = queryApi.query(flux, InfluxDataPojo.class);
            for (var dataPojo : influxDataPojos) {
                logger.info("dataPojo: " + dataPojo);
            }

            String flux2 = "from(bucket:\"logs\") |> range(start: 0)";
            List<InfluxLogPojo> influxLogPojos = queryApi.query(flux2, InfluxLogPojo.class);
            for (var logPojo : influxLogPojos) {
                logger.info("logPojo: " + logPojo);
            }
        };
    }
}
