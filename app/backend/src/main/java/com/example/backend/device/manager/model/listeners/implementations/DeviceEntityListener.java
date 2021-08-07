package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DeviceEntityListener {
    @Autowired
    private EntityListenerImplementation<Long, Device> deviceListener;

    @PostPersist
    public void postPersist(Device device) {
        deviceListener.postPersist(device);
    }

    @PostUpdate
    public void postUpdate(Device device) {
        deviceListener.postUpdate(device);
    }

    @PostRemove
    public void postRemove(Device device) {
        deviceListener.postRemove(device);
    }
}
