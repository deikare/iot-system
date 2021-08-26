package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.mappers.InfluxBucketWithTagsPojo;
import org.springframework.hateoas.RepresentationModel;

public class BucketRepresentationModel extends RepresentationModel<BucketRepresentationModel> {
    private final InfluxBucketWithTagsPojo bucket;

    public BucketRepresentationModel(InfluxBucketWithTagsPojo bucket) {
        this.bucket = bucket;
    }

    public InfluxBucketWithTagsPojo getBucket() {
        return bucket;
    }
}
