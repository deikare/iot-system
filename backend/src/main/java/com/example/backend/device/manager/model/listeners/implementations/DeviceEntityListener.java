package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import com.example.backend.utilities.loggers.abstracts.CrudOperationType;
import com.example.backend.utilities.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DeviceEntityListener {
    private final Logger logger = LoggerFactory.getLogger(DeviceEntityListener.class);

    @Autowired
    private EntityListenerImplementation<Long, Device> deviceListener;

    @PostPersist
    public void postPersist(Device device) {
        deviceListener.postPersist(device);
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.CREATE);
    }

    @PostLoad
    public void postLoad(Device device) {
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.READ);
    }


    @PostUpdate
    public void postUpdate(Device device) {
        deviceListener.postUpdate(device);
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.UPDATE);
    }

    @PostRemove
    public void postRemove(Device device) {
        deviceListener.postRemove(device);
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.DELETE);
    }
}
