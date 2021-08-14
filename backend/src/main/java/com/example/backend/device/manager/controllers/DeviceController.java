package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.DeviceModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubInDeviceNotSpecifiedException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndDeviceTypePaginationAndFilteringInterface;
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
    private final ByMasterAndDeviceTypePaginationAndFilteringInterface<Device> deviceFilteringServiceImplementation;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> deviceCrudServiceImplementation;

    public DeviceController(DeviceModelAssembler deviceModelAssembler, PagedResourcesAssembler<Device> devicePagedResourcesAssembler, ByMasterAndDeviceTypePaginationAndFilteringInterface<Device> deviceFilteringServiceImplementation, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> deviceCrudServiceImplementation) {
        this.deviceModelAssembler = deviceModelAssembler;
        this.devicePagedResourcesAssembler = devicePagedResourcesAssembler;
        this.deviceFilteringServiceImplementation = deviceFilteringServiceImplementation;
        this.deviceCrudServiceImplementation = deviceCrudServiceImplementation;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long hubId,
            @RequestParam(required = false) DeviceType deviceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Device> devices;
        Pageable pageable = PageRequest.of(page, size);
        if (name == null || name.isEmpty()) {
            if (hubId == null) {
                if (deviceType == null)
                    devices = deviceFilteringServiceImplementation.findAll(pageable);
                else devices = deviceFilteringServiceImplementation.findAllByDeviceType(deviceType, pageable);
            }
            else { //hubId not null
                if (deviceType == null)
                    devices = deviceFilteringServiceImplementation.findAllByMaster_Id(hubId, pageable);
                else devices = deviceFilteringServiceImplementation.findAllByDeviceTypeAndMaster_Id(deviceType, hubId, pageable);
            }
        }
        else { //name not null
            if (hubId == null) {
                if (deviceType == null)
                    devices = deviceFilteringServiceImplementation.findAllByNameContaining(name, pageable);
                else devices = deviceFilteringServiceImplementation.findAllByNameContainingAndDeviceType(name, deviceType, pageable);
            }
            else { //hubId not null
                if (deviceType == null)
                    devices = deviceFilteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, hubId, pageable);
                else devices = deviceFilteringServiceImplementation.findAllByNameContainingAndDeviceTypeAndMaster_Id(name, deviceType, hubId, pageable);
            }
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

    @PutMapping("/{id}")
    public EntityModel<Device> addOrChangeDevice(
            @PathVariable Long id,
            @RequestBody Device newDevice) {
        Device device;
        try {
            device = deviceCrudServiceImplementation.updateObjectById(id, newDevice);
        }
        catch (DeviceNotFoundException e) {
            device = deviceCrudServiceImplementation.addObject(newDevice);
        }
        return deviceModelAssembler.toModel(device);
    }

    @PatchMapping("/{id}")
    public EntityModel<Device> changeDevice(
            @PathVariable Long id,
            @RequestBody Device newDevice) {
        Device device;
        try {
            device = deviceCrudServiceImplementation.updateObjectById(id, newDevice);
        }
        catch (DeviceNotFoundException e) {
            throw e;
        }
        return deviceModelAssembler.toModel(device);
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
