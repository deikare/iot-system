package com.example.hubservice.management.hub.service.implementation.hub.configuration;

import com.example.hubservice.influxdb.mappers.InfluxHubStatusValue;
import com.example.hubservice.kafka.record.control.hub.HubControlType;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class HubManagementService {
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService;
    private final MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService;
    private final ApplicationContext applicationContext;
    private final TelegrafSender telegrafSender;

    private final Logger logger = LoggerFactory.getLogger(HubManagementService.class);

    public HubManagementService(MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubService, MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceService, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalService, TelegrafSender telegrafSender, ApplicationContext applicationContext) {
        this.hubService = hubService;
        this.deviceService = deviceService;
        this.controlSignalService = controlSignalService;
        this.applicationContext = applicationContext;
        this.telegrafSender = telegrafSender;

        logger.info("Starting hub initialization");

        Hub hub;
        try {
            hub = getHub();
            logger.info("Already existing hub: " + hub);
            this.telegrafSender.sendHubLogToTelegraf(hub);
        } catch (NoHubsFoundException e) {
            hub = initializeHub();
            logger.info("New hub: " + hub);
            this.telegrafSender.sendHubLogToTelegraf(hub);
        } catch (TooManyHubsExistException e) {
            initiateShutdownOnDelete();
        }

    }

    @Scheduled(fixedRate = 60000)
    public void sendHubLogToTelegraf() {
        telegrafSender.sendHubLogToTelegraf(getHub());
    }

    public Hub updateStack(Hub newStack) {
        Hub result = hubService.updateObjectById(newStack.getId(), newStack).deepCopyWithoutDependents();

        hubService.deleteAllObjects();

        Hub newVersion = hubService.addObject(result);
        String newVersionId = newVersion.getId();

        for (Device device : newStack.getDevices()) {
            Device addedDevice = deviceService.addDependentAndBindItToMasterById(device, newVersionId);

            for (ControlSignal controlSignal : device.getControlSignals()) {
                ControlSignal newControlSignal = controlSignalService.addDependentAndBindItToMasterById(controlSignal, addedDevice.getId());
            }
        }

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

    public Hub getHub() throws TooManyHubsExistException, NoHubsFoundException {
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

    public Device getDevice(Long deviceId) throws DeviceNotFoundException {
        return deviceService.findObjectById(deviceId);
    }

    public Device getRandomDevice() {
        Device result;
        List<Device> devices = deviceService.getAllObjects();

        if (devices.isEmpty())
            result = null;
        else result = devices.get(new Random().nextInt(devices.size()));

        return result;
    }

    public ControlSignal getControlSignal(Long controlSignalId) throws ControlSignalNotFoundException {
        return controlSignalService.findObjectById(controlSignalId);
    }

    public InfluxHubStatusValue getHubStatus() throws TooManyHubsExistException, NoHubsFoundException {
        return getHub().getStatus();
    }

    public String getHubId() throws TooManyHubsExistException, NoHubsFoundException {
        return getHub().getId();
    }

    public void sendDeviceDataToTelegraf(String deviceIdAsString, String measurementType, String dataAsString) {
        if (getHubStatus() == InfluxHubStatusValue.STARTED) {
            if (isDevicePresent(Long.valueOf(deviceIdAsString)))
                telegrafSender.sendDeviceDataToTelegraf(getHub(), deviceIdAsString, measurementType, dataAsString);
            else logger.info("Device not present");
        }
        else logger.info("Hub is stopped");
    }

    public void sendDeviceLogToTelegraf(String deviceIdAsString, String log) {
        if (getHubStatus() == InfluxHubStatusValue.STARTED) {
            if (isDevicePresent(Long.valueOf(deviceIdAsString)))
                telegrafSender.sendDeviceLogToTelegraf(getHub(), deviceIdAsString, log);
            else logger.info("Device not present");
        }
        else logger.info("Hub is stopped");
    }

    public void changeHubStatus(HubControlType hubControlSignal) throws TooManyHubsExistException, NoHubsFoundException {
        Hub currentHub = getHub();

        try {
            InfluxHubStatusValue statusAfterHubControl = generateProperStateAfterHubControlReceived(currentHub.getStatus(), hubControlSignal);
            currentHub.setStatus(statusAfterHubControl);
            Hub hubAfterStatusChange = hubService.updateObjectById(currentHub.getId(), currentHub);
            logger.info("Changed status of hub, after operation: " + hubAfterStatusChange);

            telegrafSender.sendHubLogToTelegraf(hubAfterStatusChange);
        }
        catch (IllegalHubControlException e) {
            logger.info(e.getMessage());
        }
    }

    private InfluxHubStatusValue generateProperStateAfterHubControlReceived(InfluxHubStatusValue currentStatus, HubControlType hubControlSignal) throws IllegalHubControlException {
        InfluxHubStatusValue result;
        switch (hubControlSignal) {
            case START -> {
                if (currentStatus == InfluxHubStatusValue.STARTED)
                    throw new IllegalHubControlException(currentStatus, hubControlSignal);
                else result = InfluxHubStatusValue.STARTED; //current status is stopped
            }
            case STOP -> {
                if (currentStatus == InfluxHubStatusValue.STARTED)
                    result = InfluxHubStatusValue.STOPPED;
                else throw new IllegalHubControlException(currentStatus, hubControlSignal); //current status is stopped
            }
            default -> throw new IllegalStateException("Unexpected value: " + hubControlSignal);
        }

        return result;
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

    private void makeShutdown(int returnCode) {
        SpringApplication.exit(applicationContext, () -> returnCode);
    }

    private Hub initializeHub() throws TooManyHubsExistException {
        Hub result;

        long actualHubCount = hubService.count();

        if (actualHubCount == 0)
            result = hubService.addObject(new Hub("place-holder", InfluxHubStatusValue.STARTED));
        else throw new TooManyHubsExistException(actualHubCount);

        return result;
    }

    private Hub deleteHub() throws TooManyHubsExistException, NoHubsFoundException {
        Hub deletedHub = getHub().deepCopyWithoutDependents();
        deletedHub.setStatus(InfluxHubStatusValue.DELETED);

        hubService.deleteAllObjects();
        return deletedHub;
    }
}
