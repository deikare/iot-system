package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.utilities.loggers.abstracts.CrudOperationType;
import com.example.backend.utilities.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DeviceEntityListener {
    private final Logger logger = LoggerFactory.getLogger(DeviceEntityListener.class);

    @PostPersist
    public void postPersist(Device device) {
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.CREATE);
    }

    @PostLoad
    public void postLoad(Device device) {
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.READ);
    }


    @PostUpdate
    public void postUpdate(Device device) {
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.UPDATE);
    }

    @PostRemove
    public void postRemove(Device device) {
        CrudServiceLogger.produceCrudServiceLog(logger, device, CrudOperationType.DELETE);
    }
}
