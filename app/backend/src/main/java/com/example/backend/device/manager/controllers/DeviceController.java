package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assembler.DeviceModelAssembler;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.DeviceServiceImplementation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceServiceImplementation deviceServiceImplementation;
    private final DeviceModelAssembler deviceModelAssembler;
    private final PagedResourcesAssembler<Device> devicePagedResourcesAssembler;

    public DeviceController(DeviceServiceImplementation deviceServiceImplementation, DeviceModelAssembler deviceModelAssembler, PagedResourcesAssembler<Device> devicePagedResourcesAssembler) {
        this.deviceServiceImplementation = deviceServiceImplementation;
        this.deviceModelAssembler = deviceModelAssembler;
        this.devicePagedResourcesAssembler = devicePagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false) Long hubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Device> devices;
        Pageable pageable = PageRequest.of(page, size);
        if (name.equals("")) {
            if (hubId.equals(null))
                devices = deviceServiceImplementation.getAllDevices(pageable);
            else devices = deviceServiceImplementation.getAllDevicesByHubId(hubId, pageable);
        }
        else {
            if (hubId.equals(null))
                devices = deviceServiceImplementation.getAllDevicesByNameContaining(name, pageable);
            else devices = deviceServiceImplementation.getAllDevicesByNameContainingAndHubId(name, hubId, pageable);
        }

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(devicePagedResourcesAssembler.toModel(devices, deviceModelAssembler));
    }
}
