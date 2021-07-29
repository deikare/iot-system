package com.example.backend.device.manager.controllers.assembler;

import com.example.backend.device.manager.controllers.DeviceController;
import com.example.backend.device.manager.controllers.HubController;
import com.example.backend.device.manager.model.Device;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeviceModelAssembler implements RepresentationModelAssembler<Device, EntityModel<Device>> {
    @NotNull
    @Override
    public EntityModel<Device> toModel(@NotNull Device device) {
        return EntityModel.of(device,
            linkTo(methodOn(DeviceController.class).one(device.getId())).withSelfRel(),
            linkTo(methodOn(DeviceController.class).all("", null, 0, 5)).withRel("devices"),
            linkTo(methodOn(HubController.class).one(device.getHub().getId())).withRel("hub"));
            // TODO add link to devices that are connected with controls ("/devices?control_ID={N}")
    }
}