package com.example.backend.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.listeners.DeviceEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(DeviceEntityListener.class)
@Audited
@Entity
public class Device implements MasterAndDependentTypeInterface<Device, ControlSignal, Hub>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "DEVICE_ID")
    private Long id;

    private String name;

    @JsonIgnore //used to avoid recursive call
    @ManyToOne/*(fetch = FetchType.LAZY)*/
    @JoinColumn(name = "HUB_ID")
    private Hub hub;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private final List<ControlSignal> controlSignals = new ArrayList<>();

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
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Device))
            return false;
        Device device = (Device) o;
        return Objects.equals(this.id, device.id) && Objects.equals(this.name, device.name)
                && Objects.equals(this.hub, device.controlSignals)
                && Objects.equals(this.deviceType, device.deviceType)
                && Objects.equals(this.controlSignals, device.controlSignals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.hub, this.deviceType, this.controlSignals);
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
    public Device deepCopy() {
        Device copy =  new Device(this.id, this.name, this.hub, this.deviceType);

        for (ControlSignal controlSignal : controlSignals) {
            copy.addDependentToDependentsList(controlSignal.deepCopy());
        }
        return copy;
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
}
