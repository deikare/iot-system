package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.ControlSignalResponseModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.*;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
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
@RequestMapping("control_responses")
public class ControlSignalResponseController {
    private final ControlSignalResponseModelAssembler modelAssembler;
    private final PagedResourcesAssembler<ControlSignalResponse> pagedResourcesAssembler;
    private final ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse, Long, Long> filteringServiceImplementation;
    private final DependentServiceImplementation<ControlSignalResponse, ControlSignal, Long, Long, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> crudServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseController.class);

    public ControlSignalResponseController(ControlSignalResponseModelAssembler modelAssembler, PagedResourcesAssembler<ControlSignalResponse> pagedResourcesAssembler, ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse, Long, Long> filteringServiceImplementation, DependentServiceImplementation<ControlSignalResponse, ControlSignal, Long, Long, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> crudServiceImplementation) {
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.filteringServiceImplementation = filteringServiceImplementation;
        this.crudServiceImplementation = crudServiceImplementation;
    }


    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long controlSignalResponseId,
            @RequestParam(required = false) String messageContent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ControlSignalResponse> result;
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isEmpty()) {
            if (controlSignalResponseId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    result = filteringServiceImplementation.findAll(pageable);
                else result = filteringServiceImplementation.findAllByMessageContentContaining(messageContent, pageable);
            }
            else { //controlSignalId not null
                if (messageContent == null)
                    result = filteringServiceImplementation.findAllByMaster_Id(controlSignalResponseId, pageable);
                else result = filteringServiceImplementation.findAllByMessageContentContainingAndMaster_Id(messageContent, controlSignalResponseId, pageable);
            }
        }
        else { //name not null
            if (controlSignalResponseId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    result = filteringServiceImplementation.findAllByNameContaining(name, pageable);
                else result = filteringServiceImplementation.findAllByNameContainingAndMessageContentContaining(name, messageContent, pageable);
            }
            else { //controlSignalId not null
                if (messageContent == null || messageContent.isEmpty())
                    result = filteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, controlSignalResponseId, pageable);
                else result = filteringServiceImplementation.findAllByNameContainingAndMessageContentContainingAndMaster_Id(name, messageContent, controlSignalResponseId, pageable);
            }
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "controlResponses", result);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(pagedResourcesAssembler.toModel(result, modelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<ControlSignalResponse> one(@PathVariable Long id) {
        ControlSignalResponse result;

        try {
            result = crudServiceImplementation.findObjectById(id);
        }
        catch (ControlSignalResponseNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.GET, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "controlResponse", result);

        return modelAssembler.toModel(result);
    }

    @PostMapping
    public EntityModel<ControlSignalResponse> newControlSignalResponse(@RequestParam(required = false) Long controlSignalResponseId,
                                                       @RequestBody ControlSignalResponse newControlSignalResponse) {
        ControlSignalResponse result;

        if (controlSignalResponseId == null) {
            try {
                result = crudServiceImplementation.addDependentAndBindItToMaster(newControlSignalResponse, newControlSignalResponse.getSentControlSignal());
            }
            catch (IllegalArgumentException e) {
                ControlSignalInResponseNotSpecifiedException controlSignalNotSpecifiedException = new ControlSignalInResponseNotSpecifiedException(newControlSignalResponse.getId());
                CrudControllerLogger.produceErrorLog(logger, HttpMethodType.POST, controlSignalNotSpecifiedException.getMessage());
                throw controlSignalNotSpecifiedException;
            }
        }
        else result = crudServiceImplementation.addDependentAndBindItToMasterById(newControlSignalResponse, controlSignalResponseId);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.POST, "controlResponse", result);

        return modelAssembler.toModel(result);
    }

    @PutMapping("/{id}")
    public EntityModel<ControlSignalResponse> addOrChangeControlSignalResponse(
            @PathVariable Long id,
            @RequestBody ControlSignalResponse newControlSignalResponse) {
        ControlSignalResponse result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newControlSignalResponse);
            CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "controlResponse", result);
        }
        catch (ControlSignalResponseNotFoundException e) {
            result = crudServiceImplementation.addObject(newControlSignalResponse);
            CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PUT, "controlResponse", result);
        }

        return modelAssembler.toModel(result);
    }

    @PatchMapping("/{id}")
    public EntityModel<ControlSignalResponse> changeControlSignalResponse(
            @PathVariable Long id,
            @RequestBody ControlSignalResponse newControlSignalResponse) {
        ControlSignalResponse result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newControlSignalResponse);
        }
        catch (ControlSignalResponseNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.PATCH, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "controlResponse", result);

        return modelAssembler.toModel(result);
    }

    @DeleteMapping("/{id}")
    void deleteControlSignalResponse(@PathVariable Long id) {
        try {
            crudServiceImplementation.deleteObjectById(id);
        }
        catch (ControlSignalResponseNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.DELETE, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "controlResponse", "true");
    }

    @DeleteMapping
    void deleteAllControlSignalResponses() {
        crudServiceImplementation.deleteAllObjects();
        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "controlResponses", "true");
    }
}
