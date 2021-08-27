package com.example.backend.utilities.builders.lists;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;

import java.util.List;

public class ListBuilder {
    public static List<Hub> hubListWithControlsFromControlSignalListBuilder(List<ControlSignal> controlSignals) {
        List<Device> devices = controlSignals.stream()
                .map(ControlSignal::getDevice)
                .distinct()
                .toList();

        return hubListWithDevicesFromDeviceListBuilder(devices);
    }

    public static List<Hub> hubListWithDevicesFromDeviceListBuilder(List<Device> devices) {
        return devices.stream()
                .map(Device::getHub)
                .distinct()
                .toList();
    }
}
