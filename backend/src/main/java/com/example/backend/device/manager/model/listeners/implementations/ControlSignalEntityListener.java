package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.ControlSignal;
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

public class ControlSignalEntityListener {
    private final Logger logger = LoggerFactory.getLogger(ControlSignalEntityListener.class);

    @Autowired
    private EntityListenerImplementation<Long, ControlSignal> controlSignalListenerImplementation;

    @PostPersist
    public void postPersist(ControlSignal controlSignal) {
        controlSignalListenerImplementation.postPersist(controlSignal);
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.CREATE);
    }

    @PostLoad
    public void postLoad(ControlSignal controlSignal) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.READ);
    }

    @PostUpdate
    public void postUpdate(ControlSignal controlSignal) {
        controlSignalListenerImplementation.postUpdate(controlSignal);
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.UPDATE);
    }

    @PostRemove
    public void postRemove(ControlSignal controlSignal) {
        controlSignalListenerImplementation.postRemove(controlSignal);
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignal, CrudOperationType.DELETE);
    }
}
