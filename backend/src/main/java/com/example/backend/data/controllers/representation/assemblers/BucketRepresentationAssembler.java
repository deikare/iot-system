package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.BucketController;
import com.example.backend.data.controllers.representation.models.BucketRepresentationModel;
import com.example.backend.data.model.mappers.InfluxBucketWithTagsPojo;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BucketRepresentationAssembler extends RepresentationModelAssemblerSupport<InfluxBucketWithTagsPojo, BucketRepresentationModel> {
    public BucketRepresentationAssembler() {
        super(BucketController.class, BucketRepresentationModel.class);
    }

    @NotNull
    @Override
    public CollectionModel<BucketRepresentationModel> toCollectionModel(@NotNull Iterable<? extends InfluxBucketWithTagsPojo> entities) {
        CollectionModel<BucketRepresentationModel> bucketRepresentationModels = super.toCollectionModel(entities);

        bucketRepresentationModels.add(linkTo(methodOn(BucketController.class).all(0, 5)).withSelfRel());

        return bucketRepresentationModels;
    }

    @NotNull
    @Override
    public BucketRepresentationModel toModel(@NotNull InfluxBucketWithTagsPojo entity) {
        BucketRepresentationModel model = new BucketRepresentationModel(entity);

        model.add(
                linkTo(methodOn(BucketController.class).one(entity.getName())).withSelfRel(),
                linkTo(methodOn(BucketController.class).all(0, 5)).withRel("buckets"));

        return model;
    }
}