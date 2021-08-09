package com.example.backend.data.controllers.assembler;

import com.example.backend.data.controllers.DataController;
import com.example.backend.data.controllers.representation.models.DataRepresentationModel;
import com.example.backend.data.model.InfluxDataPojo;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DataRepresentationAssembler extends RepresentationModelAssemblerSupport<InfluxDataPojo, DataRepresentationModel> {
    public DataRepresentationAssembler() {
        super(DataController.class, DataRepresentationModel.class);
    }

    @NotNull
    @Override
    public DataRepresentationModel toModel(@NotNull InfluxDataPojo entity) {
        DataRepresentationModel model = new DataRepresentationModel(entity);

        model.add(
                linkTo(methodOn(DataController.class).one(entity.getTime())).withSelfRel(),
                linkTo(methodOn(DataController.class).all("data", "0", "", "", "", "", "")).withRel("all-data"));

        return model;
    }

    @NotNull
    @Override
    public CollectionModel<DataRepresentationModel> toCollectionModel(@NotNull Iterable<? extends InfluxDataPojo> entities) {
        CollectionModel<DataRepresentationModel> dataRepresentationModels = super.toCollectionModel(entities);

        dataRepresentationModels.add(linkTo(methodOn(DataController.class).all("data", "0", "", "", "", "", "")).withSelfRel());

        return dataRepresentationModels;
    }
}
