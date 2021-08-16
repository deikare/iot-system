package com.example.backend.data.controllers;

import com.example.backend.data.controllers.representation.assemblers.BucketRepresentationAssembler;
import com.example.backend.data.service.InfluxBucketsService;
import com.influxdb.client.domain.Bucket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
//TODO may add bucket and task management feature
@RestController
@RequestMapping("/buckets")
public class BucketController {
    private final InfluxBucketsService influxBucketsService;
    private final BucketRepresentationAssembler assembler;
    private final PagedResourcesAssembler<Bucket> bucketPagedResourcesAssembler;


    public BucketController(InfluxBucketsService influxBucketsService, BucketRepresentationAssembler assembler, PagedResourcesAssembler<Bucket> bucketPagedResourcesAssembler) {
        this.influxBucketsService = influxBucketsService;
        this.assembler = assembler;
        this.bucketPagedResourcesAssembler = bucketPagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Bucket> result = influxBucketsService.getAllBuckets(pageable);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(bucketPagedResourcesAssembler.toModel(result, assembler));
    }
}
