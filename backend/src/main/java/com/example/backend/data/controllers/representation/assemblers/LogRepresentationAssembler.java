package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.DataController;
import com.example.backend.data.controllers.LogController;
import com.example.backend.data.controllers.representation.models.LogRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LogRepresentationAssembler extends RepresentationModelAssemblerSupport<InfluxDeviceLogPojo, LogRepresentationModel> {
    public LogRepresentationAssembler() {
        super(LogController.class, LogRepresentationModel.class);
    }

    @NotNull
    @Override
    public CollectionModel<LogRepresentationModel> toCollectionModel(@NotNull Iterable<? extends InfluxDeviceLogPojo> entities) {
        CollectionModel<LogRepresentationModel> logRepresentationModels = super.toCollectionModel(entities);

        logRepresentationModels.add(
                linkTo(methodOn(LogController.class).all(Instant.ofEpochSecond(0), null, true, null, null,null, null)).withSelfRel());

//                linkTo(methodOn(LogController.class).all("start: 0", null, null, null, null, null, null)).withSelfRel());

        return logRepresentationModels;
    }

    @NotNull
    @Override
    public LogRepresentationModel toModel(@NotNull InfluxDeviceLogPojo entity) {
        LogRepresentationModel model = new LogRepresentationModel(entity);

        model.add(
                linkTo(methodOn(LogController.class).all(Instant.ofEpochSecond(0), null, true, null, null,null, null)).withSelfRel());

        return model;
    }
}
