package com.example.backend.device.manager.controllers.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.backend.device.manager.controllers.HubController;
import com.example.backend.device.manager.model.Hub;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class HubModelAssembler implements RepresentationModelAssembler<Hub, EntityModel<Hub>> {
    @NotNull
    @Override
    public EntityModel<Hub> toModel(@NotNull Hub hub) {
        return EntityModel.of(hub,
                linkTo(methodOn(HubController.class).one(hub.getId())).withSelfRel(),
                linkTo(methodOn(HubController.class).all(null, 0, 5)).withRel("hubs"));
        // TODO add link to devices that are connected with hub ("/devices?hub_ID={N}")
    }
}
