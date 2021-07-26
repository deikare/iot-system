package com.example.backend.device.manager.controllers.assembler;

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
    public EntityModel<Device> toModel(@NotNull Device hub) {
            return EntityModel.of(hub,
            linkTo(methodOn(HubController.class).one(hub.getId())).withSelfRel(),
            linkTo(methodOn(HubController.class).all(null, 0, 5)).withRel("hubs"));
            // TODO add link to devices that are connected with hub ("/devices?hub_ID={N}")
            }
}