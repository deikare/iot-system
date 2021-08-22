package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.DataController;
import com.example.backend.data.controllers.LogController;
import com.example.backend.data.controllers.representation.models.LogTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LogTimeseriesListRepresentationAssembler extends RepresentationModelAssemblerSupport<DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo>,
                    LogTimeseriesListRepresentationModel> {

    public LogTimeseriesListRepresentationAssembler() {
        super(LogController.class, LogTimeseriesListRepresentationModel.class);
    }

    @NotNull
    @Override
    public LogTimeseriesListRepresentationModel toModel(@NotNull DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> entity) {
        LogTimeseriesListRepresentationModel model = new LogTimeseriesListRepresentationModel(entity);

        model.add(
                linkTo(methodOn(LogController.class).all("start: 0", null, null, null, null, null, null)).withRel("logs"));

        return model;
    }

    @NotNull
    public LogTimeseriesListRepresentationModel toModelConsideringBucket(@NotNull DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo> entity, @NotNull String bucket) {
        LogTimeseriesListRepresentationModel model = new LogTimeseriesListRepresentationModel(entity);

        model.add(
                linkTo(methodOn(LogController.class).all("start: 0", null, null, null, null, null, null)).withRel("logs"));

        return model;
    }

    @NotNull
    @Override
    public CollectionModel<LogTimeseriesListRepresentationModel> toCollectionModel(@NotNull Iterable<? extends DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo>> entities) {
        CollectionModel<LogTimeseriesListRepresentationModel> logRepresentationModels = super.toCollectionModel(entities);

        logRepresentationModels.add(
                linkTo(methodOn(LogController.class).all("start: 0", null, null, null, null, null, null)).withRel("logs"));

        return logRepresentationModels;
    }

    @NotNull
    public CollectionModel<LogTimeseriesListRepresentationModel> toCollectionModelConsideringBucket(@NotNull Iterable<? extends DeviceBaseTimeseriesList<String, InfluxDeviceLogPojo>> entities, @NotNull String bucket) {
        CollectionModel<LogTimeseriesListRepresentationModel> logRepresentationModels = super.toCollectionModel(entities);

        logRepresentationModels.add(
                linkTo(methodOn(LogController.class).all("start: 0", null, null, null, null, null, null)).withRel("logs"));

        return logRepresentationModels;
    }
}

