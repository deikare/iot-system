package com.example.backend.device.manager.model;

import com.example.backend.data.model.mappers.InfluxHubStatusValue;
import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterTypeInterface;
import com.example.backend.device.manager.model.listeners.HubEntityListener;

import javax.persistence.*;
import java.util.*;

@EntityListeners(HubEntityListener.class)
@Entity
public class Hub implements MasterTypeInterface<Hub, Device>, KafkaRecordInterface<String> {
    @Id
    @Column(name = "HUB_ID")
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private InfluxHubStatusValue status;

    //TODO use transactional in service layer to assure correct items management
    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Device> devices = new HashSet<>();

    public Hub(String id, String name, InfluxHubStatusValue status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Hub() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InfluxHubStatusValue getStatus() {
        return status;
    }

    public void setStatus(InfluxHubStatusValue status) {
        this.status = status;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    @Override
    public String toString() {
        return "Hub{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Hub hub))
            return false;
        return Objects.equals(this.id, hub.id) && Objects.equals(this.name, hub.name)
                && Objects.equals(this.devices, hub.devices) && Objects.equals(this.status, hub.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.devices, this.status);
    }


    @Override
    public Hub update(Hub patch) {
        if (patch != null) {
            updateName(patch);
            updateStatus(patch);
        }
        return this;
    }

    private void updateName(Hub patch) {
        String newName = String.valueOf(patch.getName());
        if (newName != null && !newName.isEmpty())
            setName(newName);
    }

    private void updateStatus(Hub patch) {
        InfluxHubStatusValue newStatus = patch.getStatus();
        if (newStatus != null)
            setStatus(newStatus);
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
