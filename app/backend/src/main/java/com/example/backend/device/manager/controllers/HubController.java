package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assembler.HubModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.HubServiceImplementation;
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
    private final HubModelAssembler hubModelAssembler;
    private final PagedResourcesAssembler<Hub> hubPagedResourcesAssembler;

    public HubController(HubServiceImplementation hubServiceImplementation, HubModelAssembler hubModelAssembler, PagedResourcesAssembler<Hub> hubPagedResourcesAssembler) {
        this.hubServiceImplementation = hubServiceImplementation;
        this.hubModelAssembler = hubModelAssembler;
        this.hubPagedResourcesAssembler = hubPagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Hub> hubs;
        Pageable pageable = PageRequest.of(page, size);
        if (name == null)
            hubs = hubServiceImplementation.getAllHubs(pageable);
        else hubs = hubServiceImplementation.getAllHubsContainingName(name, pageable);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(hubPagedResourcesAssembler.toModel(hubs, hubModelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<Hub> one(@PathVariable Long id) {
        Hub hub = null;
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
        Hub hub = null;
        try {
            hub = hubServiceImplementation.findHubById(id);
            hub.setName(newHub.getName());
        }
        catch (HubNotFoundException e) {
            hub = hubServiceImplementation.addHub(newHub);
        }
        return hubModelAssembler.toModel(hub);
    }

    @DeleteMapping("/{id}")
    void deleteHub(@PathVariable Long id) {
        hubServiceImplementation.deleteHubById(id);
    }
}
