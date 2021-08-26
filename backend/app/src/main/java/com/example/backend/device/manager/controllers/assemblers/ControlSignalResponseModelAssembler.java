package com.example.backend.device.manager.controllers.assemblers;

import com.example.backend.device.manager.controllers.ControlSignalController;
import com.example.backend.device.manager.controllers.ControlSignalResponseController;
import com.example.backend.device.manager.model.ControlSignalResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ControlSignalResponseModelAssembler implements RepresentationModelAssembler<ControlSignalResponse, EntityModel<ControlSignalResponse>> {
    @NotNull
    @Override
    public EntityModel<ControlSignalResponse> toModel(@NotNull ControlSignalResponse controlSignalResponse) {
        return EntityModel.of(controlSignalResponse,
                linkTo(methodOn(ControlSignalResponseController.class).one(controlSignalResponse.getId())).withSelfRel(),
                linkTo(methodOn(ControlSignalResponseController.class).all(null, null, null, 0, 5)).withRel("control-responses"),
                linkTo(methodOn(ControlSignalController.class).one(controlSignalResponse.getSentControlSignal().getId())).withRel("control-signal"));
    }
}
