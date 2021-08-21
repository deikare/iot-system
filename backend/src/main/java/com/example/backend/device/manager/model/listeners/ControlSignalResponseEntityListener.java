package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.utilities.loggers.abstracts.CrudOperationType;
import com.example.backend.utilities.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalResponseEntityListener {
    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseEntityListener.class);


    @PostPersist
    public void postPersist(ControlSignalResponse controlSignalResponse) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, CrudOperationType.CREATE);
    }

    @PostLoad
    public void postLoad(ControlSignalResponse controlSignalResponse) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, CrudOperationType.READ);
    }


    @PostUpdate
    public void postUpdate(ControlSignalResponse controlSignalResponse) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, CrudOperationType.UPDATE);
    }

    @PostRemove
    public void postRemove(ControlSignalResponse controlSignalResponse) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, CrudOperationType.DELETE);
    }
}