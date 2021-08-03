package com.example.backend.device.manager.model;

import com.example.backend.device.manager.model.properties.DeviceProperties;
import com.example.backend.device.manager.model.properties.HubProperties;
import com.example.backend.device.manager.service.interfaces.MasterTypeInterface;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Entity
public class Hub implements MasterTypeInterface<Hub, Device> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "HUB_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
    private final List<Device> devices = new ArrayList<>();

    public Hub(String name) {
        this.name = name;
    }

    public Hub() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    //TODO delete this method
    public boolean removeDeviceFromDeviceList(Device device) {
        return devices.remove(device);
    }

    @Override
    public String toString() {
        return "Hub{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", devices=" + devices +
                '}';
    }

    @Override
    public Hub update(Properties patch) {
        updateName(patch);
        return this;
    }

    private void updateName(Properties patch) {
        String newName = String.valueOf(patch.get(DeviceProperties.NAME));
        if (!newName.isEmpty())
            setName(newName);
    }

    @Override
    public Hub addDependentToDependentsList(Device dependent) {
        devices.add(dependent);
        return this;
    }

    @Override
    public boolean removeDependentFromDependentsList(Device dependent) {
        return devices.remove(dependent);
    }
}
