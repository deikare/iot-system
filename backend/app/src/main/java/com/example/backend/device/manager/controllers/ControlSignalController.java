package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.ControlSignalModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.*;
import com.example.backend.device.manager.kafka.services.senders.EntityCrudSenderService;
import com.example.backend.device.manager.model.*;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import com.example.backend.device.manager.service.implementation.utilities.EntityLazilyFetchedFieldsInitializer;
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

import java.util.List;

//TODO szyfrowanie ssl kafka - serwer, zamienić postgresa w hubie na sqlite, zrównoleglić fetch z influxa
@RestController
@RequestMapping("/control_signals")
public class ControlSignalController {
    private final ControlSignalModelAssembler modelAssembler;
    private final PagedResourcesAssembler<ControlSignal> pagedResourcesAssembler;
    private final ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal, Long, Long> filteringServiceImplementation;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation;

    private final EntityLazilyFetchedFieldsInitializer entityLazilyFetchedFieldsInitializer;

    private final EntityCrudSenderService<String, Hub> hubSender;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalController.class);

    public ControlSignalController(ControlSignalModelAssembler modelAssembler, PagedResourcesAssembler<ControlSignal> pagedResourcesAssembler, ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal, Long, Long> filteringServiceImplementation, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> crudServiceImplementation, EntityLazilyFetchedFieldsInitializer entityLazilyFetchedFieldsInitializer, EntityCrudSenderService<String, Hub> hubSender) {
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.filteringServiceImplementation = filteringServiceImplementation;
        this.crudServiceImplementation = crudServiceImplementation;
        this.entityLazilyFetchedFieldsInitializer = entityLazilyFetchedFieldsInitializer;
        this.hubSender = hubSender;
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
    public EntityModel<ControlSignal> newControlSignal(@RequestParam Long deviceId,
                                         @RequestBody ControlSignal newControlSignal) {
        ControlSignal result;

        try {
            result = crudServiceImplementation.addDependentAndBindItToMasterById(newControlSignal, deviceId);
        }
        catch (DeviceNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.GET, e.getMessage());
            throw e;
        }

        hubSender.postUpdate(entityLazilyFetchedFieldsInitializer.generateFetchedHubBasedOnControlSignal(result));

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
        }
        catch (ControlSignalNotFoundException e) {
            result = crudServiceImplementation.addObject(newControlSignal);
        }
        catch (EntityNotModifiedException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.PUT, e.getMessage());
            throw e;
        }

        hubSender.postUpdate(entityLazilyFetchedFieldsInitializer.generateFetchedHubBasedOnControlSignal(result));

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PUT, "controlSignal", result);

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
        catch (ControlSignalNotFoundException | EntityNotModifiedException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.PATCH, e.getMessage());
            throw e;
        }

        hubSender.postUpdate(entityLazilyFetchedFieldsInitializer.generateFetchedHubBasedOnControlSignal(result));

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "controlSignal", result);

        return modelAssembler.toModel(result);
    }

    @DeleteMapping("/{id}")
    void deleteControlSignal(@PathVariable Long id) {
        try {
            ControlSignal deletedControlSignal = crudServiceImplementation.deleteObjectByIdAndReturnDeletedObject(id);
            hubSender.postUpdate(entityLazilyFetchedFieldsInitializer.generateFetchedHubBasedOnControlSignal(deletedControlSignal));
        }
        catch (ControlSignalNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.DELETE, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "controlSignal", "true");
    }

    @DeleteMapping
    void deleteAllControlSignals() {
        List<ControlSignal> deletedControlSignals = crudServiceImplementation.deleteAllObjectsAndReturnThemListed();

        hubSender.postUpdates(entityLazilyFetchedFieldsInitializer.generateFetchedHubsBasedOnControlSignals(deletedControlSignals));

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "controlSignals", "true");
    }
}
