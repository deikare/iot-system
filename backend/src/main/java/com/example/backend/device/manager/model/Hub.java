package com.example.backend.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterTypeInterface;
import com.example.backend.device.manager.model.listeners.HubEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EntityListeners(HubEntityListener.class)
@Entity
public class Hub implements MasterTypeInterface<Hub, Device>, KafkaRecordInterface<String> {
    @Id
    @Column(name = "HUB_ID")
    private String id;

    private String name;

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
    private final List<Device> devices = new ArrayList<>();

    public Hub(String id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Device> getDevices() {
        return devices;
    }

    @Override
    public String toString() {
        return "Hub{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Hub))
            return false;
        Hub hub = (Hub) o;
        return Objects.equals(this.id, hub.id) && Objects.equals(this.name, hub.name)
                && Objects.equals(this.devices, hub.devices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.devices);
    }


    @Override
    public Hub update(Hub patch) {
        if (patch != null)
            updateName(patch);
        return this;
    }

    private void updateName(Hub patch) {
        String newName = String.valueOf(patch.getName());
        if (newName != null && !newName.isEmpty())
            setName(newName);
    }

    @Override
    public Hub deepCopy() {
        Hub copy =  new Hub(this.id, this.name);

        for (Device device : devices) {
            copy.addDependentToDependentsList(device.deepCopy());
        }
        return copy;
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
