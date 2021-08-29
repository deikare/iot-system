package com.example.hubservice.management.hub.service.implementation.hub.configuration;

import com.example.hubservice.influxdb.mappers.InfluxHubStatusValue;
import com.example.hubservice.management.hub.exceptions.*;
import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.model.Device;
import com.example.hubservice.management.hub.model.Hub;
import com.example.hubservice.management.hub.service.implementation.crud.DependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HubConfigurationService {
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService;

    private final Logger logger = LoggerFactory.getLogger(HubConfigurationService.class);

    public HubConfigurationService(MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService) {
        this.hubService = hubService;
        this.deviceService = deviceService;
        this.controlSignalService = controlSignalService;
    }

    public Hub updateStack(Hub newStack) throws WrongNumberOfHubsException {
        Hub hub = getHub();

        deviceService.deleteAllObjects();
        for (Device device : hub.getDevices()) {
            deviceService.addDependentAndBindItToMaster(device, hub);

            for (ControlSignal controlSignal : device.getControlSignals())
                controlSignalService.addDependentAndBindItToMaster(controlSignal, device);
        }
        return hub;
    }


    public Hub initializeHub() throws WrongNumberOfHubsException {
        Hub result;

        long actualHubCount = hubService.count();

        if (actualHubCount == 0)
            result = hubService.addObject(new Hub("place-holder", InfluxHubStatusValue.CREATED));
        else throw new WrongNumberOfHubsException(0L, actualHubCount);

        return result;
    }

    public InfluxHubStatusValue getHubStatus() throws WrongNumberOfHubsException, NoHubsFoundException {
        return getHub().getStatus();
    }

    public String getHubId() throws WrongNumberOfHubsException, NoHubsFoundException {
        return getHub().getId();
    }

    public void deleteHub() throws WrongNumberOfHubsException, NoHubsFoundException {
        Hub deletedHub = getHub();

        hubService.deleteAllObjects();
    }

    public Hub getHub() throws WrongNumberOfHubsException, NoHubsFoundException {
        Hub result;

        long actualHubCount = hubService.count();

        if (actualHubCount != 0) {
            if (actualHubCount == 1)
                result = hubService.getAllObjects().get(0);
            else throw new WrongNumberOfHubsException(1L, actualHubCount);
        }
        else throw new NoHubsFoundException();

        return result;
    }
}
