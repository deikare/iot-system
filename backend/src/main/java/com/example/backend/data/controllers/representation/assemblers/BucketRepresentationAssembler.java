package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.BucketController;
import com.example.backend.data.controllers.representation.models.BucketRepresentationModel;
import com.influxdb.client.domain.Bucket;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BucketRepresentationAssembler extends RepresentationModelAssemblerSupport<Bucket, BucketRepresentationModel> {
    public BucketRepresentationAssembler() {
        super(BucketController.class, BucketRepresentationModel.class);
    }

    @NotNull
    @Override
    public CollectionModel<BucketRepresentationModel> toCollectionModel(@NotNull Iterable<? extends Bucket> entities) {
        CollectionModel<BucketRepresentationModel> bucketRepresentationModels = super.toCollectionModel(entities);

        bucketRepresentationModels.add(linkTo(methodOn(BucketController.class).all()).withSelfRel());

        return bucketRepresentationModels;
    }

    @NotNull
    @Override
    public BucketRepresentationModel toModel(@NotNull Bucket entity) {
        BucketRepresentationModel model = new BucketRepresentationModel(entity);

        model.add(
                linkTo(methodOn(BucketController.class).all()).withRel("buckets"));

        return model;
    }
}