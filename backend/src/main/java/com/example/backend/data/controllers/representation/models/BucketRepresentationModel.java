package com.example.backend.data.controllers.representation.models;

import com.influxdb.client.domain.Bucket;
import org.springframework.hateoas.RepresentationModel;

public class BucketRepresentationModel extends RepresentationModel<BucketRepresentationModel> {
    private final Bucket bucket;

    public BucketRepresentationModel(Bucket bucket) {
        this.bucket = bucket;
    }

    public Bucket getBucket() {
        return bucket;
    }
}
