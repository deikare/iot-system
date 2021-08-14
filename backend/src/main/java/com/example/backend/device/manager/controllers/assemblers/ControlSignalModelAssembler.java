package com.example.backend.device.manager.controllers.assemblers;

import com.example.backend.device.manager.controllers.ControlSignalController;
import com.example.backend.device.manager.controllers.ControlSignalResponseController;
import com.example.backend.device.manager.controllers.DeviceController;
import com.example.backend.device.manager.model.ControlSignal;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ControlSignalModelAssembler implements RepresentationModelAssembler<ControlSignal, EntityModel<ControlSignal>> {
    @NotNull
    @Override
    public EntityModel<ControlSignal> toModel(@NotNull ControlSignal controlSignal) {
        return EntityModel.of(controlSignal,
                linkTo(methodOn(ControlSignalController.class).one(controlSignal.getId())).withSelfRel(),
                linkTo(methodOn(ControlSignalController.class).all(null, null, null, 0, 5)).withRel("control-signals"),
                linkTo(methodOn(DeviceController.class).one(controlSignal.getDevice().getId())).withRel("device"),
                linkTo(methodOn(ControlSignalResponseController.class).all(null, controlSignal.getId(), null, 0, 5)).withRel("control-responses"));

    }
}
