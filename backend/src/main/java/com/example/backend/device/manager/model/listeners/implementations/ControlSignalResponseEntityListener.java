package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import com.example.backend.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalResponseEntityListener {
    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseEntityListener.class);

    @Autowired
    private EntityListenerImplementation<Long, ControlSignalResponse> controlSignalResponseListenerImplementation;

    @PostPersist
    public void postPersist(ControlSignalResponse controlSignalResponse) {
        controlSignalResponseListenerImplementation.postPersist(controlSignalResponse);
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, "create");
    }

    @PostLoad
    public void postLoad(ControlSignalResponse controlSignalResponse) {
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, "read");
    }


    @PostUpdate
    public void postUpdate(ControlSignalResponse controlSignalResponse) {
        controlSignalResponseListenerImplementation.postUpdate(controlSignalResponse);
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, "update");
    }

    @PostRemove
    public void postRemove(ControlSignalResponse controlSignalResponse) {
        controlSignalResponseListenerImplementation.postRemove(controlSignalResponse);
        CrudServiceLogger.produceCrudServiceLog(logger, controlSignalResponse, "delete");
    }
}
