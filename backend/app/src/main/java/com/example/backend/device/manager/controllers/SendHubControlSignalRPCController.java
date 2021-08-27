package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.kafka.record.control.hub.HubControlType;
import com.example.backend.device.manager.kafka.services.senders.HubControlSenderService;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.CrudControllerLogger;
import com.example.backend.utilities.loggers.abstracts.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/send_hub_control")
public class SendHubControlSignalRPCController {
    private final HubControlSenderService sender;
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(SendHubControlSignalRPCController.class);

    public SendHubControlSignalRPCController(HubControlSenderService sender, MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation) {
        this.sender = sender;
        this.crudServiceImplementation = crudServiceImplementation;
    }


    @PostMapping("/{id}")
    public ResponseEntity<String> pushHubControlToKafka(@PathVariable String id, @RequestParam HubControlType controlType) {
        Hub result;
        try {
            result = crudServiceImplementation.findObjectById(id);
        }
        catch (HubNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.POST, e.getMessage());
            throw e;
        }

        sender.sendControl(result, controlType);

        logger.info("Sent: " + controlType + "to " + result);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.POST, "controlSignal", result);

        return new ResponseEntity<>("Hub control sent", HttpStatus.OK);
    }
}
