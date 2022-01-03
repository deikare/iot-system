package com.example.backend.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.listeners.DeviceEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@EntityListeners(DeviceEntityListener.class)
@Entity
public class Device implements MasterAndDependentTypeInterface<Device, ControlSignal, Hub>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "DEVICE_ID")
    private Long id;

    private String name;

    @JsonIgnore //used to avoid recursive call
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HUB_ID")
    private Hub hub;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<ControlSignal> controlSignals = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private Device(Long id, String name, Hub hub, DeviceType deviceType) {
        this.id = id;
        this.name = name;
        this.hub = hub;
        this.deviceType = deviceType;
    }

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

    public Set<ControlSignal> getControlSignals() {
        return controlSignals;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deviceType=" + deviceType +
                ", hub=" + hub +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Device device))
            return false;
        return Objects.equals(this.id, device.id) && Objects.equals(this.name, device.name)
                && Objects.equals(this.hub, device.hub)
                && Objects.equals(this.deviceType, device.deviceType)
                && Objects.equals(this.controlSignals, device.controlSignals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public Device update(Device patch) {
        if (patch != null) {
            this.updateName(patch);
            this.updateHub(patch);
            this.updateDeviceType(patch);
        }
        return this;
    }

    private void updateName(Device patch) {
        String newName = String.valueOf(patch.getName());
        if (newName != null && !newName.isEmpty())
            setName(newName);
    }

    private void updateHub(Device patch) {
        Hub newHub = patch.getHub();
        if (newHub != null)
            setHub(newHub);
    }

    private void updateDeviceType(Device patch) {
        DeviceType newDeviceType = patch.getDeviceType();
        if (newDeviceType != null)
            setDeviceType(newDeviceType);
    }

    @Override
    public Device addDependentToDependentsList(ControlSignal dependent) {
        controlSignals.add(dependent);
        return this;
    }

    @Override
    public boolean removeDependentFromDependentsList(ControlSignal dependent) {
        return controlSignals.remove(dependent);
    }

    @Override
    public void setMaster(Hub master) {
        setHub(master);
        this.hub.addDependentToDependentsList(this);
    }
}
