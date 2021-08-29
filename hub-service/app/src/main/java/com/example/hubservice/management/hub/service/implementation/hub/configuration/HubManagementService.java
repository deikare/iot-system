package com.example.hubservice.management.hub.service.implementation.hub.configuration;

import com.example.hubservice.influxdb.mappers.InfluxHubStatusValue;
import com.example.hubservice.management.hub.exceptions.*;
import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.model.Device;
import com.example.hubservice.management.hub.model.Hub;
import com.example.hubservice.management.hub.service.implementation.crud.DependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.telegraf.TelegrafSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class HubManagementService {
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService;
    private final ApplicationContext applicationContext;
    private final TelegrafSender telegrafSender;

    private Hub hub;

    private final Logger logger = LoggerFactory.getLogger(HubManagementService.class);

    public HubManagementService(MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService, TelegrafSender telegrafSender, ApplicationContext applicationContext) {
        this.hubService = hubService;
        this.deviceService = deviceService;
        this.controlSignalService = controlSignalService;
        this.applicationContext = applicationContext;
        this.telegrafSender = telegrafSender;

        logger.info("Starting hub initialization");

        try {
            hub = getHubOnStartup();
            logger.info("Already existing hub: " + hub);
        } catch (NoHubsFoundException e) {
            hub = initializeHub();
            logger.info("New hub: " + hub);
        } catch (TooManyHubsExistException e) {
            //TODO add shutdown hook
        }

        this.telegrafSender.sendHubLogToTelegraf(hub);
    }

    public Hub updateStack(Hub newStack) {
        Hub result = hubService.updateObjectById(newStack.getId(), newStack);

        logger.info("Updating stack with new hub: " + result);

        deviceService.deleteAllObjects();
        for (Device device : newStack.getDevices()) {
            deviceService.addDependentAndBindItToMaster(device, result);

            for (ControlSignal controlSignal : device.getControlSignals())
                controlSignalService.addDependentAndBindItToMaster(controlSignal, device);
        }

        hub = result;

        return result;
    }

    public boolean isHubPresent(String hubId) {
        return hubService.isPresent(hubId);
    }

    public boolean isDevicePresent(Long deviceId) {
        return deviceService.isPresent(deviceId);
    }

    public boolean isControlSignalPresent(Long controlSignalId) {
        return controlSignalService.isPresent(controlSignalId);
    }

    public Hub getHub() {
        return hub;
    }

    public InfluxHubStatusValue getHubStatus() throws TooManyHubsExistException, NoHubsFoundException {
        return getHub().getStatus();
    }

    public String getHubId() throws TooManyHubsExistException, NoHubsFoundException {
        return getHub().getId();
    }

    public void initiateShutdownOnDelete() {
        int returnCode = 0;
        try {
            Hub deletedHub = deleteHub();
            telegrafSender.sendHubLogToTelegraf(deletedHub);
            logger.info("Received delete signal, shutdown gracefully");
        }
        catch (TooManyHubsExistException | NoHubsFoundException e) {
            logger.info("Error during shutdown: " + e.getMessage());
            returnCode = 103;
        }

        makeShutdown(returnCode);
    }

    private Hub initializeHub() throws TooManyHubsExistException {
        Hub result;

        long actualHubCount = hubService.count();

        if (actualHubCount == 0)
            result = hubService.addObject(new Hub("place-holder", InfluxHubStatusValue.CREATED));
        else throw new TooManyHubsExistException(actualHubCount);

        return result;
    }

    private Hub getHubOnStartup() throws TooManyHubsExistException, NoHubsFoundException {
        Hub result;

        long actualHubCount = hubService.count();

        if (actualHubCount != 0) {
            if (actualHubCount == 1)
                result = hubService.getAllObjects().get(0);
            else throw new TooManyHubsExistException(actualHubCount);
        }
        else throw new NoHubsFoundException();

        return result;
    }

    private Hub deleteHub() throws TooManyHubsExistException, NoHubsFoundException {
        Hub deletedHub = getHub().deepCopyWithoutDependents();
        deletedHub.setStatus(InfluxHubStatusValue.DELETED);

        hubService.deleteAllObjects();
        return deletedHub;
    }

    private void makeShutdown(int returnCode) {
        SpringApplication.exit(applicationContext, () -> returnCode);
    }
}
