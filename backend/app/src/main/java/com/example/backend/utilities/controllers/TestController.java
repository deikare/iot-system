package com.example.backend.utilities.controllers;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.kafka.services.senders.EntityCrudSenderService;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/tests")
public class TestController {
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService;

    private final EntityCrudSenderService<String, Hub> hubSender;

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    public TestController(MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService, EntityCrudSenderService<String, Hub> hubSender) {
        this.hubService = hubService;
        this.deviceService = deviceService;
        this.controlSignalService = controlSignalService;
        this.hubSender = hubSender;
    }

    @GetMapping("/create")
    public ResponseEntity<String> createStacksForAllHubs() {
        List<Hub> hubs = hubService.getAllObjects();
        int hubNo = 1;

        for (Hub hub : hubs) {
            int amountOfDevices = ThreadLocalRandom.current().nextInt(1, 10);

            for (int i = 1; i <= amountOfDevices; i++) {
                Device device = new Device("D_" + hubNo + "_" + i, hub, DeviceType.BOTH);
                logger.info("Preloading devices: " + deviceService.addDependentAndBindItToMaster(device, hub));

                int amountOfControlSignals = ThreadLocalRandom.current().nextInt(1, 10);

                for (int j = 1; j <= amountOfControlSignals; j++) {
                    ControlSignal controlSignal = new ControlSignal("Cs_" + hubNo + "_" + i + "_" + j, randomString(4), device);
                    logger.info("Preloading controlSignals: " + controlSignalService.addDependentAndBindItToMaster(controlSignal, device));

                    device.addDependentToDependentsList(controlSignal);
                }
                hub.addDependentToDependentsList(device);
            }
            hubNo++;
        }

        hubSender.postUpdates(hubs);

        return new ResponseEntity<>("Hubs loaded sent", HttpStatus.OK);
    }

    private String randomString(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
