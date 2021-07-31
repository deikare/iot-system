package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assembler.HubModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.HubServiceImplementation;
import com.example.backend.device.manager.service.implementation.HubServiceImplementation2;
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
    private final HubServiceImplementation hubServiceImplementation;
    private final HubServiceImplementation2 hubServiceImplementation2;
    private final HubModelAssembler hubModelAssembler;
    private final PagedResourcesAssembler<Hub> hubPagedResourcesAssembler;

    public HubController(HubServiceImplementation hubServiceImplementation, HubServiceImplementation2 hubServiceImplementation2, HubModelAssembler hubModelAssembler, PagedResourcesAssembler<Hub> hubPagedResourcesAssembler) {
        this.hubServiceImplementation = hubServiceImplementation;
        this.hubServiceImplementation2 = hubServiceImplementation2;
        this.hubModelAssembler = hubModelAssembler;
        this.hubPagedResourcesAssembler = hubPagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Hub> hubs;
        Pageable pageable = PageRequest.of(page, size);
        if (name.equals(""))
            hubs = hubServiceImplementation.getAllHubs(pageable);
        else hubs = hubServiceImplementation.getAllHubsByNameContaining(name, pageable);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(hubPagedResourcesAssembler.toModel(hubs, hubModelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<Hub> one(@PathVariable Long id) {
        Hub hub;
        try {
            hub = hubServiceImplementation.findHubById(id);
        }
        catch (HubNotFoundException e) {
            throw e;
        }
        return hubModelAssembler.toModel(hub);
    }

    @PostMapping
    public EntityModel<Hub> newHub(@RequestBody Hub newHub) {
        return hubModelAssembler.toModel(hubServiceImplementation.addHub(newHub));
    }

    @PutMapping("/{id}")
    public EntityModel<Hub> addOrChangeNameOfHub(
            @PathVariable Long id,
            @RequestBody Hub newHub) {
        Hub hub;
        try {
            hub = hubServiceImplementation.updateHubNameById(id, newHub.getName());
        }
        catch (HubNotFoundException e) {
            hub = hubServiceImplementation.addHub(newHub);
        }
        return hubModelAssembler.toModel(hub);
    }

    @PatchMapping("/{id}")
    public EntityModel<Hub> changeNameOfHub(
            @PathVariable Long id,
            @RequestBody String name) {
        Hub hub;
        try {
            hub = hubServiceImplementation.updateHubNameById(id, name);
        }
        catch (HubNotFoundException e) {
            throw e;
        }
        return hubModelAssembler.toModel(hub);
    }

    @DeleteMapping("/{id}")
    void deleteHub(@PathVariable Long id) {
        try {
            hubServiceImplementation.deleteHubById(id);
        }
        catch (HubNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping
    void deleteAllHubs() {
        hubServiceImplementation.deleteAllHubs();
    }
}
