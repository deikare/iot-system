package com.example.backend.device.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.security.SecureRandom;

@Configuration
@EnableJpaAuditing
public class LoadDatabase {
    Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

/*    @Bean
    CommandLineRunner initializeHubs(
            MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubServiceImplementation,
            MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceServiceImplementation,
            MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalServiceImplementation,
            DependentServiceImplementation<ControlSignalResponse, ControlSignal, Long, Long, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlResponseServiceImplementation) {
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

                        int amountOfResponses = ThreadLocalRandom.current().nextInt(1, 5);
                        for(int l = 0; l < amountOfResponses; l++) {
                            ControlSignalResponse controlSignalResponse = new ControlSignalResponse("Csr" + l, randomString(4), controlSignal);
                            logger.info("Preloading responses: " + controlResponseServiceImplementation.addDependentAndBindItToMaster(controlSignalResponse, controlSignal));
                        }
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
