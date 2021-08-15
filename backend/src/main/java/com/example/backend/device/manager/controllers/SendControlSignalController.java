package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.kafka.producer.KafkaControlSignalSender;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("send_control")
public class SendControlSignalController {
    private final KafkaControlSignalSender sender;
    private final MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation;
    private final String topic = "sent-controls";

    public SendControlSignalController(KafkaControlSignalSender sender, MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation) {
        this.sender = sender;
        this.crudServiceImplementation = crudServiceImplementation;
    }
    //TODO get that informs how to use this endpoint
    /*@GetMapping
    public ResponseEntity<String> getInfo() {
        return pushControlToKafkaById()
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getInfoById(@PathVariable Long id) {
        return pushControlToKafkaById(id)
    }*/

    @PostMapping("/{id}")
    public ResponseEntity<String> pushControlToKafkaById(@PathVariable Long id) {
        ControlSignal controlSignal;
        try {
            controlSignal = crudServiceImplementation.findObjectById(id);
        }
        catch (ControlSignalNotFoundException e) {
            throw e;
        }

        ProducerRecord<Long, ControlSignal> record = new ProducerRecord<>(topic, controlSignal.getId(), controlSignal);
        sender.sendKafkaRecord(record);
        return new ResponseEntity<>("Control signal sent", HttpStatus.OK);
    }
}