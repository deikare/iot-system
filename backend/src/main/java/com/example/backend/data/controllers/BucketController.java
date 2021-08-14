package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assembler.BucketRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.BucketRepresentationModel;
import com.example.backend.data.service.InfluxBucketsService;
import com.influxdb.client.domain.Bucket;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//TODO may add bucket and task management feature
@RestController
@RequestMapping("/buckets")
public class BucketController {
    private final InfluxBucketsService influxBucketsService;
    private final BucketRepresentationAssembler assembler;

    public BucketController(InfluxBucketsService influxBucketsService, BucketRepresentationAssembler assembler) {
        this.influxBucketsService = influxBucketsService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<BucketRepresentationModel> all() {
        List<Bucket> queryResult = influxBucketsService.getAllBuckets();

        return assembler.toCollectionModel(queryResult);
    }
}
