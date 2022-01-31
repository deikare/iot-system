package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.DataController;
import com.example.backend.data.controllers.representation.models.DataTimeseriesListRepresentationModel;
import com.example.backend.data.model.mappers.InfluxDeviceDataPojo;
import com.example.backend.data.model.timeseries.DeviceBaseTimeseriesList;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

        model.add(linkTo(methodOn(DataController.class)
                        .all("data", Instant.ofEpochSecond(0), null, true, null, null,null, null))
                        .withRel("all-data"));

        return model;
    }

    @NotNull
    public DataTimeseriesListRepresentationModel toModelConsideringQueryParams(@NotNull DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo> entity,
                                                                               @NotNull String bucket, Long limit,
                                                                               List<String> hubIds, List<String> deviceIds, List<String> types) {
        DataTimeseriesListRepresentationModel model = new DataTimeseriesListRepresentationModel(entity);

        model.add(linkTo(methodOn(DataController.class)
                .all(bucket, Instant.ofEpochSecond(0), null, true, null, null,null, null))
                .withRel("all-data"),
                linkTo(methodOn(DataController.class) //there is little offset in beginning and end because _start <= _t < _end
                        .all(bucket, entity.getStart().minus(2, ChronoUnit.HOURS), entity.getStart(),
                                true, limit, hubIds, deviceIds, types))
                        .withRel("prev"),
                linkTo(methodOn(DataController.class)
                        .all(bucket, entity.getEnd(), entity.getEnd().plus(2, ChronoUnit.HOURS),
                                false, limit, hubIds, deviceIds, types))
                        .withRel("next"));

        return model;
    }

    @NotNull
    @Override
    public CollectionModel<DataTimeseriesListRepresentationModel> toCollectionModel(@NotNull Iterable<? extends DeviceBaseTimeseriesList<Double, InfluxDeviceDataPojo>> entities) {
        CollectionModel<DataTimeseriesListRepresentationModel> dataRepresentationModels = super.toCollectionModel(entities);

        dataRepresentationModels.add(linkTo(methodOn(DataController.class)
                .all("data", Instant.ofEpochSecond(0), null, true, null, null,null, null))
                .withSelfRel());

        return dataRepresentationModels;
    }
}
