package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assembler.ControlSignalModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceInControlSignalNotSpecifiedException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.model.*;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/control_signals")
public class ControlSignalController {
    private final ControlSignalModelAssembler controlSignalModelAssembler;
    private final PagedResourcesAssembler<ControlSignal> controlSignalPagedResourcesAssembler;
    private final ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal> controlSignalFilteringServiceImplementation;
    private final MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalCrudServiceImplementation;

    public ControlSignalController(ControlSignalModelAssembler controlSignalModelAssembler, PagedResourcesAssembler<ControlSignal> controlSignalPagedResourcesAssembler, ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal> controlSignalFilteringServiceImplementation, MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalCrudServiceImplementation) {
        this.controlSignalModelAssembler = controlSignalModelAssembler;
        this.controlSignalPagedResourcesAssembler = controlSignalPagedResourcesAssembler;
        this.controlSignalFilteringServiceImplementation = controlSignalFilteringServiceImplementation;
        this.controlSignalCrudServiceImplementation = controlSignalCrudServiceImplementation;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) String messageContent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ControlSignal> controlSignals;
        Pageable pageable = PageRequest.of(page, size);
        if (name == null || name.isEmpty()) {
            if (deviceId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    controlSignals = controlSignalFilteringServiceImplementation.findAll(pageable);
                else controlSignals = controlSignalFilteringServiceImplementation.findAllByMessageContentContaining(messageContent, pageable);
            }
            else { //deviceId not null
                if (messageContent == null)
                    controlSignals = controlSignalFilteringServiceImplementation.findAllByMaster_Id(deviceId, pageable);
                else controlSignals = controlSignalFilteringServiceImplementation.findAllByMessageContentContainingAndMaster_Id(messageContent, deviceId, pageable);
            }
        }
        else { //name not null
            if (deviceId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    controlSignals = controlSignalFilteringServiceImplementation.findAllByNameContaining(name, pageable);
                else controlSignals = controlSignalFilteringServiceImplementation.findAllByNameContainingAndMessageContentContaining(name, messageContent, pageable);
            }
            else { //deviceId not null
                if (messageContent == null || messageContent.isEmpty())
                    controlSignals = controlSignalFilteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, deviceId, pageable);
                else controlSignals = controlSignalFilteringServiceImplementation.findAllByNameContainingAndMessageContentContainingAndMaster_Id(name, messageContent, deviceId, pageable);
            }
        }

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(controlSignalPagedResourcesAssembler.toModel(controlSignals, controlSignalModelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<ControlSignal> one(@PathVariable Long id) {
        ControlSignal controlSignal;
        try {
            controlSignal = controlSignalCrudServiceImplementation.findObjectById(id);
        }
        catch (ControlSignalNotFoundException e) {
            throw e;
        }
        return controlSignalModelAssembler.toModel(controlSignal);
    }

    @PostMapping
    public EntityModel<ControlSignal> newControlSignal(@RequestParam(required = false) Long deviceId,
                                         @RequestBody ControlSignal newControlSignal) {
        ControlSignal result;

        if (deviceId == null)
            try {
                result = controlSignalCrudServiceImplementation.addDependentAndBindItToMaster(newControlSignal, newControlSignal.getDevice());
            }
            catch (IllegalArgumentException e) {
                throw new DeviceInControlSignalNotSpecifiedException(newControlSignal.getId());
            }
        else result = controlSignalCrudServiceImplementation.addDependentAndBindItToMasterById(newControlSignal, deviceId);

        return controlSignalModelAssembler.toModel(result);
    }

    @PutMapping("/{id}")
    public EntityModel<ControlSignal> addOrChangeControlSignal(
            @PathVariable Long id,
            @RequestBody ControlSignal newControlSignal) {
        ControlSignal controlSignal;
        try {
            controlSignal = controlSignalCrudServiceImplementation.updateObjectById(id, newControlSignal);
        }
        catch (ControlSignalNotFoundException e) {
            controlSignal = controlSignalCrudServiceImplementation.addObject(newControlSignal);
        }
        return controlSignalModelAssembler.toModel(controlSignal);
    }

    @PatchMapping("/{id}")
    public EntityModel<ControlSignal> changeControlSignal(
            @PathVariable Long id,
            @RequestBody ControlSignal newControlSignal) {
        ControlSignal controlSignal;
        try {
            controlSignal = controlSignalCrudServiceImplementation.updateObjectById(id, newControlSignal);
        }
        catch (ControlSignalNotFoundException e) {
            throw e;
        }
        return controlSignalModelAssembler.toModel(controlSignal);
    }

    @DeleteMapping("/{id}")
    void deleteControlSignal(@PathVariable Long id) {
        try {
            controlSignalCrudServiceImplementation.deleteObjectById(id);
        }
        catch (ControlSignalNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping
    void deleteAllControlSignals() {
        controlSignalCrudServiceImplementation.deleteAllObjects();
    }
}
