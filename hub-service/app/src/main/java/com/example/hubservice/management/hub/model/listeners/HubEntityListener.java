package com.example.hubservice.management.hub.model.listeners;

import com.example.hubservice.management.hub.model.Hub;
import com.example.hubservice.utilities.loggers.abstracts.CrudOperationType;
import com.example.hubservice.utilities.loggers.abstracts.CrudServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class HubEntityListener {
    private final Logger logger = LoggerFactory.getLogger(HubEntityListener.class);


    @PostPersist
    public void postPersist(Hub hub) {
        CrudServiceLogger.produceCrudServiceLog(logger, hub, CrudOperationType.CREATE);
    }

    @PostLoad
    public void postLoad(Hub hub) {
        CrudServiceLogger.produceCrudServiceLog(logger, hub, CrudOperationType.READ);
    }

    @PostUpdate
    public void postUpdate(Hub hub) {
        CrudServiceLogger.produceCrudServiceLog(logger, hub, CrudOperationType.UPDATE);
    }

    @PostRemove
    public void postRemove(Hub hub) {
        CrudServiceLogger.produceCrudServiceLog(logger, hub, CrudOperationType.DELETE);
    }
}
