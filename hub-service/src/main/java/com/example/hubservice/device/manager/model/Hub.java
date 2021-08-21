package com.example.hubservice.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterTypeInterface;
import com.example.backend.device.manager.model.listeners.implementations.HubEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hub implements MasterTypeInterface<Hub, com.example.backend.device.manager.model.Device>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "HUB_ID")
    private Long id;

    private String name;

    @JsonIgnore
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

    @Override
    public String toString() {
        return "Hub{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", devices=" + devices +
                '}';
    }

    @Override
    public Hub update(Hub patch) {
        if (patch != null)
            updateName(patch);
        return this;
    } //lol

    private void updateName(Hub patch) {
        String newName = String.valueOf(patch.getName());
        if (newName != null && !newName.isEmpty())
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