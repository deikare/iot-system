package com.example.backend.device.manager.model;

import com.example.backend.device.manager.model.properties.DeviceProperties;
import com.example.backend.device.manager.service.interfaces.DependentTypeInterface;
import com.example.backend.device.manager.service.interfaces.MasterTypeInterface;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Entity
public class Device implements MasterTypeInterface<Device, ControlSignal>, DependentTypeInterface<Device, Hub> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DEVICE_ID")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HUB_ID")
    private Hub hub;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private final List<ControlSignal> controlSignals = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    public Device(String name, Hub hub, DeviceType deviceType) {
        this.name = name;
        this.hub = hub;
        this.deviceType = deviceType;
    }

    public Device() {

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

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public List<ControlSignal> getControlSignals() {
        return controlSignals;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public Device update(Properties patch) {
        this.updateName(patch);
        this.updateHub(patch);
        return this;
    }

    private void updateName(Properties patch) {
        String newName = String.valueOf(patch.get(DeviceProperties.NAME));
        if (!newName.isEmpty())
            setName(newName);
    }

    private void updateHub(Properties patch) {
        Hub newHub = (Hub) patch.get(DeviceProperties.HUB);
        if (newHub instanceof Hub)
            setHub(newHub);
    }

    @Override
    public Device addDependentToDependentsList(ControlSignal dependent) {
        return null;
    }

    @Override
    public boolean removeDependentFromDependentsList(ControlSignal dependent) {
        return false;
    }
}
