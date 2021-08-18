package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.ControlSignalModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceInControlSignalNotSpecifiedException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.model.*;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.CrudControllerLogger;
import com.example.backend.utilities.loggers.abstracts.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ControlSignalModelAssembler modelAssembler;
    private final PagedResourcesAssembler<ControlSignal> pagedResourcesAssembler;
    private final ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal, Long, Long> filteringServiceImplementation;
    private final MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalController.class);

    public ControlSignalController(ControlSignalModelAssembler modelAssembler, PagedResourcesAssembler<ControlSignal> pagedResourcesAssembler, ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal, Long, Long> filteringServiceImplementation, MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation) {
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.filteringServiceImplementation = filteringServiceImplementation;
        this.crudServiceImplementation = crudServiceImplementation;
    }


    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) String messageContent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ControlSignal> result;
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isEmpty()) {
            if (deviceId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    result = filteringServiceImplementation.findAll(pageable);
                else result = filteringServiceImplementation.findAllByMessageContentContaining(messageContent, pageable);
            }
            else { //deviceId not null
                if (messageContent == null)
                    result = filteringServiceImplementation.findAllByMaster_Id(deviceId, pageable);
                else result = filteringServiceImplementation.findAllByMessageContentContainingAndMaster_Id(messageContent, deviceId, pageable);
            }
        }
        else { //name not null
            if (deviceId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    result = filteringServiceImplementation.findAllByNameContaining(name, pageable);
                else result = filteringServiceImplementation.findAllByNameContainingAndMessageContentContaining(name, messageContent, pageable);
            }
            else { //deviceId not null
                if (messageContent == null || messageContent.isEmpty())
                    result = filteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, deviceId, pageable);
                else result = filteringServiceImplementation.findAllByNameContainingAndMessageContentContainingAndMaster_Id(name, messageContent, deviceId, pageable);
            }
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "controlSignals", result);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(pagedResourcesAssembler.toModel(result, modelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<ControlSignal> one(@PathVariable Long id) {
        ControlSignal result;

        try {
            result = crudServiceImplementation.findObjectById(id);
        }
        catch (ControlSignalNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.GET, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "controlSignal", result);

        return modelAssembler.toModel(result);
    }

    @PostMapping
    public EntityModel<ControlSignal> newControlSignal(@RequestParam(required = false) Long deviceId,
                                         @RequestBody ControlSignal newControlSignal) {
        ControlSignal result;

        if (deviceId == null) {
            try {
                result = crudServiceImplementation.addDependentAndBindItToMaster(newControlSignal, newControlSignal.getDevice());
            }
            catch (IllegalArgumentException e) {
                DeviceInControlSignalNotSpecifiedException deviceNotSpecifiedException = new DeviceInControlSignalNotSpecifiedException(newControlSignal.getId());
                CrudControllerLogger.produceErrorLog(logger, HttpMethodType.POST, deviceNotSpecifiedException.getMessage());
                throw deviceNotSpecifiedException;
            }
        }
        else result = crudServiceImplementation.addDependentAndBindItToMasterById(newControlSignal, deviceId);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.POST, "controlSignal", result);

        return modelAssembler.toModel(result);
    }

    @PutMapping("/{id}")
    public EntityModel<ControlSignal> addOrChangeControlSignal(
            @PathVariable Long id,
            @RequestBody ControlSignal newControlSignal) {
        ControlSignal result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newControlSignal);
            CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "controlSignal", result);
        }
        catch (ControlSignalNotFoundException e) {
            result = crudServiceImplementation.addObject(newControlSignal);
            CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PUT, "controlSignal", result);
        }

        return modelAssembler.toModel(result);
    }

    @PatchMapping("/{id}")
    public EntityModel<ControlSignal> changeControlSignal(
            @PathVariable Long id,
            @RequestBody ControlSignal newControlSignal) {
        ControlSignal result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newControlSignal);
        }
        catch (ControlSignalNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.PATCH, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "controlSignal", result);

        return modelAssembler.toModel(result);
    }

    @DeleteMapping("/{id}")
    void deleteControlSignal(@PathVariable Long id) {
        try {
            crudServiceImplementation.deleteObjectById(id);
        }
        catch (ControlSignalNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.DELETE, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "controlSignal", "true");
    }

    @DeleteMapping
    void deleteAllControlSignals() {
        crudServiceImplementation.deleteAllObjects();
        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "controlSignals", "true");
    }
}
