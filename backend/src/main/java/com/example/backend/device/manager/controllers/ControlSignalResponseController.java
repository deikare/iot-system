package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.ControlSignalResponseModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.*;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
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
@RequestMapping("control_responses")
public class ControlSignalResponseController {
    private final ControlSignalResponseModelAssembler controlSignalResponseModelAssembler;
    private final PagedResourcesAssembler<ControlSignalResponse> controlSignalResponsePagedResourcesAssembler;
    private final ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse> controlSignalResponseFilteringServiceImplementation;
    private final DependentServiceImplementation<ControlSignalResponse, ControlSignal, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlSignalResponseCrudServiceImplementation;

    public ControlSignalResponseController(ControlSignalResponseModelAssembler controlSignalResponseModelAssembler, PagedResourcesAssembler<ControlSignalResponse> controlSignalResponsePagedResourcesAssembler, ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse> controlSignalResponseFilteringServiceImplementation, DependentServiceImplementation<ControlSignalResponse, ControlSignal, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlSignalResponseCrudServiceImplementation) {
        this.controlSignalResponseModelAssembler = controlSignalResponseModelAssembler;
        this.controlSignalResponsePagedResourcesAssembler = controlSignalResponsePagedResourcesAssembler;
        this.controlSignalResponseFilteringServiceImplementation = controlSignalResponseFilteringServiceImplementation;
        this.controlSignalResponseCrudServiceImplementation = controlSignalResponseCrudServiceImplementation;
    }


    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long controlSignalResponseId,
            @RequestParam(required = false) String messageContent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ControlSignalResponse> controlSignalResponses;
        Pageable pageable = PageRequest.of(page, size);
        if (name == null || name.isEmpty()) {
            if (controlSignalResponseId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAll(pageable);
                else controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByMessageContentContaining(messageContent, pageable);
            }
            else { //controlSignalId not null
                if (messageContent == null)
                    controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByMaster_Id(controlSignalResponseId, pageable);
                else controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByMessageContentContainingAndMaster_Id(messageContent, controlSignalResponseId, pageable);
            }
        }
        else { //name not null
            if (controlSignalResponseId == null) {
                if (messageContent == null || messageContent.isEmpty())
                    controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByNameContaining(name, pageable);
                else controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByNameContainingAndMessageContentContaining(name, messageContent, pageable);
            }
            else { //controlSignalId not null
                if (messageContent == null || messageContent.isEmpty())
                    controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, controlSignalResponseId, pageable);
                else controlSignalResponses = controlSignalResponseFilteringServiceImplementation.findAllByNameContainingAndMessageContentContainingAndMaster_Id(name, messageContent, controlSignalResponseId, pageable);
            }
        }

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(controlSignalResponsePagedResourcesAssembler.toModel(controlSignalResponses, controlSignalResponseModelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<ControlSignalResponse> one(@PathVariable Long id) {
        ControlSignalResponse controlSignalResponse;
        try {
            controlSignalResponse = controlSignalResponseCrudServiceImplementation.findObjectById(id);
        }
        catch (ControlSignalResponseNotFoundException e) {
            throw e;
        }
        return controlSignalResponseModelAssembler.toModel(controlSignalResponse);
    }

    @PostMapping
    public EntityModel<ControlSignalResponse> newControlSignalResponse(@RequestParam(required = false) Long controlSignalResponseId,
                                                       @RequestBody ControlSignalResponse newControlSignalResponse) {
        ControlSignalResponse result;

        if (controlSignalResponseId == null)
            try {
                result = controlSignalResponseCrudServiceImplementation.addDependentAndBindItToMaster(newControlSignalResponse, newControlSignalResponse.getSentControlSignal());
            }
            catch (IllegalArgumentException e) {
                throw new ControlSignalInResponseNotSpecifiedException(newControlSignalResponse.getId());
            }
        else result = controlSignalResponseCrudServiceImplementation.addDependentAndBindItToMasterById(newControlSignalResponse, controlSignalResponseId);

        return controlSignalResponseModelAssembler.toModel(result);
    }

    @PutMapping("/{id}")
    public EntityModel<ControlSignalResponse> addOrChangeControlSignalResponse(
            @PathVariable Long id,
            @RequestBody ControlSignalResponse newControlSignalResponse) {
        ControlSignalResponse controlSignalResponse;
        try {
            controlSignalResponse = controlSignalResponseCrudServiceImplementation.updateObjectById(id, newControlSignalResponse);
        }
        catch (ControlSignalResponseNotFoundException e) {
            controlSignalResponse = controlSignalResponseCrudServiceImplementation.addObject(newControlSignalResponse);
        }
        return controlSignalResponseModelAssembler.toModel(controlSignalResponse);
    }

    @PatchMapping("/{id}")
    public EntityModel<ControlSignalResponse> changeControlSignalResponse(
            @PathVariable Long id,
            @RequestBody ControlSignalResponse newControlSignalResponse) {
        ControlSignalResponse controlSignalResponse;
        try {
            controlSignalResponse = controlSignalResponseCrudServiceImplementation.updateObjectById(id, newControlSignalResponse);
        }
        catch (ControlSignalResponseNotFoundException e) {
            throw e;
        }
        return controlSignalResponseModelAssembler.toModel(controlSignalResponse);
    }

    @DeleteMapping("/{id}")
    void deleteControlSignalResponse(@PathVariable Long id) {
        try {
            controlSignalResponseCrudServiceImplementation.deleteObjectById(id);
        }
        catch (ControlSignalResponseNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping
    void deleteAllControlSignalResponses() {
        controlSignalResponseCrudServiceImplementation.deleteAllObjects();
    }
}
