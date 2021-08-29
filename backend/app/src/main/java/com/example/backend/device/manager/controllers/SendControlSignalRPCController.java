package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.kafka.services.senders.ControlSignalSenderService;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.CrudControllerLogger;
import com.example.backend.utilities.loggers.abstracts.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send_device_control")
public class SendControlSignalRPCController {
    private final ControlSignalSenderService sender;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(SendControlSignalRPCController.class);

    public SendControlSignalRPCController(ControlSignalSenderService sender, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation) {
        this.sender = sender;
        this.crudServiceImplementation = crudServiceImplementation;
    }


    @PostMapping("/{id}")
    public ResponseEntity<String> pushControlToKafkaById(@PathVariable Long id) {
        ControlSignal result;
        try {
            result = crudServiceImplementation.findObjectById(id);
        }
        catch (ControlSignalNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.POST, e.getMessage());
            throw e;
        }

        sender.sendNewControlSignal(result);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.POST, "controlSignal", result);

        return new ResponseEntity<>("Control signal sent", HttpStatus.OK);
    }
}
