package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.DataController;
import com.example.backend.data.controllers.representation.models.DataTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceDataPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DataTimeseriesListRepresentationAssembler
        extends RepresentationModelAssemblerSupport<DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo>,
        DataTimeseriesListRepresentationModel> {

    public DataTimeseriesListRepresentationAssembler() {
        super(DataController.class, DataTimeseriesListRepresentationModel.class);
    }

    @NotNull
    @Override
    public DataTimeseriesListRepresentationModel toModel(@NotNull DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> entity) {
        DataTimeseriesListRepresentationModel model = new DataTimeseriesListRepresentationModel(entity);

        model.add(
                linkTo(methodOn(DataController.class).all("data", "start: 0", null, null, null, null,null, null)).withRel("all-data"));

        return model;
    }

    @NotNull
    public DataTimeseriesListRepresentationModel toModelConsideringBucket(@NotNull DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> entity, @NotNull String bucket) {
        DataTimeseriesListRepresentationModel model = new DataTimeseriesListRepresentationModel(entity);

        model.add(
                linkTo(methodOn(DataController.class).all(bucket, "start: 0", null, null, null, null, null, null)).withRel("all-data"));

        return model;
    }

    @NotNull
    @Override
    public CollectionModel<DataTimeseriesListRepresentationModel> toCollectionModel(@NotNull Iterable<? extends DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo>> entities) {
        CollectionModel<DataTimeseriesListRepresentationModel> dataRepresentationModels = super.toCollectionModel(entities);

        dataRepresentationModels.add(linkTo(methodOn(DataController.class).all("data", "start: 0", null, null, null, null, null, null)).withSelfRel());

        return dataRepresentationModels;
    }

    @NotNull
    public CollectionModel<DataTimeseriesListRepresentationModel> toCollectionModelConsideringBucket(@NotNull Iterable<? extends DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo>> entities, @NotNull String bucket) {
        CollectionModel<DataTimeseriesListRepresentationModel> dataRepresentationModels = super.toCollectionModel(entities);

        dataRepresentationModels.add(linkTo(methodOn(DataController.class).all(bucket, "start: 0", null, null, null, null, null, null)).withSelfRel());

        return dataRepresentationModels;
    }
}
