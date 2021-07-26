package com.example.backend.device.manager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hub {
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

    public void addDeviceToDeviceList(Device device) {
        devices.add(device);
    }

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
}
