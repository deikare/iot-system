package com.example.hubservice.management.hub.model.listeners;

import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.utilities.loggers.abstracts.CrudOperationType;
import com.example.hubservice.utilities.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalEntityListener {
    private final Logger logger = LoggerFactory.getLogger(ControlSignalEntityListener.class);

    @PostPersist
    public void postPersist(ControlSignal controlSignal) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.CREATE);
    }

    @PostLoad
    public void postLoad(ControlSignal controlSignal) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.READ);
    }

    @PostUpdate
    public void postUpdate(ControlSignal controlSignal) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.UPDATE);
    }

    @PostRemove
    public void postRemove(ControlSignal controlSignal) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.DELETE);
    }
}
