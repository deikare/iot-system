package com.example.backend.device.manager.controllers;

import com.example.backend.device.manager.controllers.assemblers.DeviceModelAssembler;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubInDeviceNotSpecifiedException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.kafka.services.senders.EntityCrudSenderService;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndDeviceTypePaginationAndFilteringInterface;
import com.example.backend.utilities.builders.lists.ListBuilder;
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

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceModelAssembler modelAssembler;
    private final PagedResourcesAssembler<Device> pagedResourcesAssembler;
    private final ByMasterAndDeviceTypePaginationAndFilteringInterface<Device, Long, String> filteringServiceImplementation;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> crudServiceImplementation;

    private final EntityCrudSenderService<String, Hub> hubSender;

    private final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    public DeviceController(DeviceModelAssembler modelAssembler, PagedResourcesAssembler<Device> pagedResourcesAssembler, ByMasterAndDeviceTypePaginationAndFilteringInterface<Device, Long, String> filteringServiceImplementation, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> crudServiceImplementation, EntityCrudSenderService<String, Hub> hubSender) {
        this.modelAssembler = modelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.filteringServiceImplementation = filteringServiceImplementation;
        this.crudServiceImplementation = crudServiceImplementation;
        this.hubSender = hubSender;
    }


    @GetMapping
    public ResponseEntity all(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String hubId,
            @RequestParam(required = false) DeviceType deviceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Device> result;
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isEmpty()) {
            if (hubId == null) {
                if (deviceType == null)
                    result = filteringServiceImplementation.findAll(pageable);
                else result = filteringServiceImplementation.findAllByDeviceType(deviceType, pageable);
            }
            else { //hubId not null
                if (deviceType == null)
                    result = filteringServiceImplementation.findAllByMaster_Id(hubId, pageable);
                else result = filteringServiceImplementation.findAllByDeviceTypeAndMaster_Id(deviceType, hubId, pageable);
            }
        }
        else { //name not null
            if (hubId == null) {
                if (deviceType == null)
                    result = filteringServiceImplementation.findAllByNameContaining(name, pageable);
                else result = filteringServiceImplementation.findAllByNameContainingAndDeviceType(name, deviceType, pageable);
            }
            else { //hubId not null
                if (deviceType == null)
                    result = filteringServiceImplementation.findAllByNameContainingAndMaster_Id(name, hubId, pageable);
                else result = filteringServiceImplementation.findAllByNameContainingAndDeviceTypeAndMaster_Id(name, deviceType, hubId, pageable);
            }
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "devices", result);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(pagedResourcesAssembler.toModel(result, modelAssembler));
    }

    @GetMapping("/{id}")
    public EntityModel<Device> one(@PathVariable Long id) {
        Device result;
        try {
            result = crudServiceImplementation.findObjectById(id);
        }
        catch (DeviceNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.GET, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.GET, "device", result);

        return modelAssembler.toModel(result);
    }

    @PostMapping
    public EntityModel<Device> newDevice(@RequestParam(required = false) String hubId,
                                         @RequestBody Device device) {
        Device result;

        if (hubId == null)
            try {
                result = crudServiceImplementation.addDependentAndBindItToMaster(device, device.getHub());
            }
            catch (IllegalArgumentException e) {
                HubInDeviceNotSpecifiedException hubNotSpecifiedException = new HubInDeviceNotSpecifiedException(device.getId());
                CrudControllerLogger.produceErrorLog(logger, HttpMethodType.POST, hubNotSpecifiedException.getMessage());
                throw hubNotSpecifiedException;
            }
        else result = crudServiceImplementation.addDependentAndBindItToMasterById(device, hubId);

        hubSender.postUpdate(result.getHub());

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.POST, "device", result);

        return modelAssembler.toModel(result);
    }

    @PutMapping("/{id}")
    public EntityModel<Device> addOrChangeDevice(
            @PathVariable Long id,
            @RequestBody Device newDevice) {
        Device result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newDevice);
        }
        catch (DeviceNotFoundException e) {
            result = crudServiceImplementation.addObject(newDevice);
        }

        hubSender.postUpdate(result.getHub());

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PUT, "device", result);

        return modelAssembler.toModel(result);
    }

    @PatchMapping("/{id}")
    public EntityModel<Device> changeDevice(
            @PathVariable Long id,
            @RequestBody Device newDevice) {
        Device result;

        try {
            result = crudServiceImplementation.updateObjectById(id, newDevice);
        }
        catch (DeviceNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.PATCH, e.getMessage());
            throw e;
        }

        hubSender.postUpdate(result.getHub());

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.PATCH, "device", result);

        return modelAssembler.toModel(result);
    }

    @DeleteMapping("/{id}")
    void deleteDevice(@PathVariable Long id) {
        try {
            Device deletedDevice = crudServiceImplementation.deleteObjectByIdAndReturnDeletedObject(id);
            hubSender.postUpdate(deletedDevice.getHub());
        }
        catch (DeviceNotFoundException e) {
            CrudControllerLogger.produceErrorLog(logger, HttpMethodType.DELETE, e.getMessage());
            throw e;
        }

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "device", "true");
    }

    @DeleteMapping
    void deleteAllDevices() {
        List<Device> deletedDevices = crudServiceImplementation.deleteAllObjectsAndReturnThemListed();

        List<Hub> hubs = ListBuilder.hubListWithDevicesFromDeviceListBuilder(deletedDevices);

        hubSender.postUpdates(hubs);

        CrudControllerLogger.produceCrudControllerLog(logger, HttpMethodType.DELETE, "devices", "true");
    }
}
