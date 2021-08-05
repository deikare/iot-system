package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assembler.DeviceModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubInDeviceNotSpecifiedException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceModelAssembler deviceModelAssembler;
    private final PagedResourcesAssembler<Device> devicePagedResourcesAssembler;
    private final ByMasterPaginationAndFilteringInterface<Device> deviceFilteringServiceImplementation;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> deviceCrudServiceImplementation;

    public DeviceController(DeviceModelAssembler deviceModelAssembler, PagedResourcesAssembler<Device> devicePagedResourcesAssembler, ByMasterPaginationAndFilteringInterface<Device> deviceFilteringServiceImplementation, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> deviceCrudServiceImplementation) {
        this.deviceModelAssembler = deviceModelAssembler;
        this.devicePagedResourcesAssembler = devicePagedResourcesAssembler;
        this.deviceFilteringServiceImplementation = deviceFilteringServiceImplementation;
        this.deviceCrudServiceImplementation = deviceCrudServiceImplementation;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Long hubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Device> devices;
        Pageable pageable = PageRequest.of(page, size);
        if (name.isEmpty()) {
            if (hubId == null)
                devices = deviceFilteringServiceImplementation.findAll(pageable);
            else devices = deviceFilteringServiceImplementation.findAllByMaster_Id(hubId, pageable);
        }
        else {
            if (hubId == null)
                devices = deviceFilteringServiceImplementation.findAllByNameContaining(name, pageable);
            else devices = deviceFilteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, hubId, pageable);
        }

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(devicePagedResourcesAssembler.toModel(devices, deviceModelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<Device> one(@PathVariable Long id) {
        Device device;
        try {
            device = deviceCrudServiceImplementation.findObjectById(id);
        }
        catch (DeviceNotFoundException e) {
            throw e;
        }
        return deviceModelAssembler.toModel(device);
    }

    @PostMapping
    public EntityModel<Device> newDevice(@RequestParam(required = false) Long hubId,
                                         @RequestBody Device device) {
        Device result;

        if (hubId == null)
            try {
                result = deviceCrudServiceImplementation.addDependentAndBindItToMaster(device, device.getHub());
            }
            catch (IllegalArgumentException e) {
                throw new HubInDeviceNotSpecifiedException(device.getId());
            }
        else result = deviceCrudServiceImplementation.addDependentAndBindItToMasterById(device, hubId);

        return deviceModelAssembler.toModel(result);
    }

    @DeleteMapping("/{id}")
    void deleteDevice(@PathVariable Long id) {
        try {
            deviceCrudServiceImplementation.deleteObjectById(id);
        }
        catch (DeviceNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping
    void deleteAllDevices() {
        deviceCrudServiceImplementation.deleteAllObjects();
    }
}
