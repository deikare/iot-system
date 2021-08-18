package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.HubModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.BasePaginationAndFilteringServiceImplementation;
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

import java.util.UUID;

@RestController
@RequestMapping("/hubs")
public class HubController {
    private final HubModelAssembler modelAssembler;
    private final PagedResourcesAssembler<Hub> pagedResourcesAssembler;
    private final BasePaginationAndFilteringServiceImplementation<Hub, String> filteringServiceImplementation;
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(HubController.class);

    public HubController(HubModelAssembler modelAssembler, PagedResourcesAssembler<Hub> pagedResourcesAssembler, BasePaginationAndFilteringServiceImplementation<Hub, String> filteringServiceImplementation, MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation) {
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.filteringServiceImplementation = filteringServiceImplementation;
        this.crudServiceImplementation = crudServiceImplementation;
    }


    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Hub> result;
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.equals(""))
            result = filteringServiceImplementation.findAll(pageable);
        else result = filteringServiceImplementation.findAllByNameContaining(name, pageable);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "hubs", result);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(pagedResourcesAssembler.toModel(result, modelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<Hub> one(@PathVariable String id) {
        Hub result;

        try {
            result = crudServiceImplementation.findObjectById(id);
        }
        catch (HubNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.GET, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "hub", result);

        return modelAssembler.toModel(result);
    }

    @PostMapping
    public EntityModel<Hub> newHub(@RequestBody Hub newHub) {
        Hub result = crudServiceImplementation.addObject(newHub);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.POST, "hub", result);

        return modelAssembler.toModel(crudServiceImplementation.addObject(newHub));
    }

    @PutMapping("/{id}")
    public EntityModel<Hub> addOrChangeHub(
            @PathVariable String id,
            @RequestBody Hub newHub) {
        Hub result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newHub);
            CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "hub", result);
        }
        catch (HubNotFoundException e) {
            result = crudServiceImplementation.addObject(newHub);
            CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PUT, "hub", result);
        }

        return modelAssembler.toModel(result);
    }

    @PatchMapping("/{id}")
    public EntityModel<Hub> changeHub(
            @PathVariable String id,
            @RequestBody Hub newHub) {
        Hub result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newHub);
        }
        catch (HubNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.PATCH, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "hub", result);

        return modelAssembler.toModel(result);
    }

    @DeleteMapping("/{id}")
    void deleteHub(@PathVariable String id) {
        try {
            crudServiceImplementation.deleteObjectById(id);
        }
        catch (HubNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.DELETE, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "hub", "true");
    }

    @DeleteMapping
    void deleteAllHubs() {
        crudServiceImplementation.deleteAllObjects();
        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "hubs", "true");
    }
}
