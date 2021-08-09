package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assembler.HubModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.BasePaginationAndFilteringServiceImplementation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hubs")
public class HubController {
    private final HubModelAssembler hubModelAssembler;
    private final PagedResourcesAssembler<Hub> hubPagedResourcesAssembler;
    private final BasePaginationAndFilteringServiceImplementation<Hub> hubFilteringServiceImplementation;
    private final MasterServiceImplementation<Hub, Device, HubNotFoundException> hubCrudServiceImplementation;

    public HubController(HubModelAssembler hubModelAssembler, PagedResourcesAssembler<Hub> hubPagedResourcesAssembler, BasePaginationAndFilteringServiceImplementation<Hub> hubFilteringServiceImplementation, MasterServiceImplementation<Hub, Device, HubNotFoundException> hubCrudServiceImplementation) {
        this.hubModelAssembler = hubModelAssembler;
        this.hubPagedResourcesAssembler = hubPagedResourcesAssembler;
        this.hubFilteringServiceImplementation = hubFilteringServiceImplementation;
        this.hubCrudServiceImplementation = hubCrudServiceImplementation;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Hub> hubs;
        Pageable pageable = PageRequest.of(page, size);

        if (name.equals(""))
            hubs = hubFilteringServiceImplementation.findAll(pageable);
        else hubs = hubFilteringServiceImplementation.findAllByNameContaining(name, pageable);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(hubPagedResourcesAssembler.toModel(hubs, hubModelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<Hub> one(@PathVariable Long id) {
        Hub hub;
        try {
            hub = hubCrudServiceImplementation.findObjectById(id);
        }
        catch (HubNotFoundException e) {
            throw e;
        }
        return hubModelAssembler.toModel(hub);
    }

    @PostMapping
    public EntityModel<Hub> newHub(@RequestBody Hub newHub) {
        return hubModelAssembler.toModel(hubCrudServiceImplementation.addObject(newHub));
    }

    //TODO Hub to properties converter needed
    @PutMapping("/{id}")
    public EntityModel<Hub> addOrChangeHub(
            @PathVariable Long id,
            @RequestBody Hub newHub) {
        Hub hub;
        try {
            hub = hubCrudServiceImplementation.updateObjectById(id, null);
        }
        catch (HubNotFoundException e) {
            hub = hubCrudServiceImplementation.addObject(newHub);
        }
        return hubModelAssembler.toModel(hub);
    }

    @PatchMapping("/{id}")
    public EntityModel<Hub> changeHub(
            @PathVariable Long id,
            @RequestBody Hub newHub) {
        Hub hub;
        try {
            hub = hubCrudServiceImplementation.updateObjectById(id, null);
        }
        catch (HubNotFoundException e) {
            throw e;
        }
        return hubModelAssembler.toModel(hub);
    }

    @DeleteMapping("/{id}")
    void deleteHub(@PathVariable Long id) {
        try {
            hubCrudServiceImplementation.deleteObjectById(id);
        }
        catch (HubNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping
    void deleteAllHubs() {
        hubCrudServiceImplementation.deleteAllObjects();
    }
}
