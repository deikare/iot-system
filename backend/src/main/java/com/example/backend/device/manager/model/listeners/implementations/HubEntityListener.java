package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import com.example.backend.device.manager.service.configs.control.response.ControlSignalResponseServiceImplementationConfig;
import com.example.backend.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class HubEntityListener {
    private final Logger logger = LoggerFactory.getLogger(HubEntityListener.class);

    @Autowired
    private EntityListenerImplementation<Long, Hub> hubListener;

    @PostPersist
    public void postPersist(Hub hub) {
        hubListener.postPersist(hub);
        CrudServiceLogger.produceCrudServiceLog(logger, hub, "create");
    }

    @PostLoad
    public void postLoad(Hub hub) {
        CrudServiceLogger.produceCrudServiceLog(logger, hub, "read");
    }

    @PostUpdate
    public void postUpdate(Hub hub) {
        hubListener.postUpdate(hub);
        CrudServiceLogger.produceCrudServiceLog(logger, hub, "update");
    }

    @PostRemove
    public void postRemove(Hub hub) {
        hubListener.postRemove(hub);
        CrudServiceLogger.produceCrudServiceLog(logger, hub, "delete");
    }
}
