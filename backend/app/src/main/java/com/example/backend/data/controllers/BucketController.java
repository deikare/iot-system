package com.example.backend.data.controllers;

import com.example.backend.data.controllers.exceptions.BucketNotFoundException;
import com.example.backend.data.controllers.representation.assemblers.BucketRepresentationAssembler;
import com.example.backend.data.controllers.representation.models.BucketRepresentationModel;
import com.example.backend.data.model.mappers.InfluxBucketWithTagsPojo;
import com.example.backend.data.service.InfluxBucketsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/buckets")
public class BucketController {
    private final InfluxBucketsService influxBucketsService;
    private final BucketRepresentationAssembler assembler;
    private final PagedResourcesAssembler<InfluxBucketWithTagsPojo> bucketPagedResourcesAssembler;

    private final Logger logger = LoggerFactory.getLogger(BucketController.class);


    public BucketController(InfluxBucketsService influxBucketsService, BucketRepresentationAssembler assembler, PagedResourcesAssembler<InfluxBucketWithTagsPojo> bucketPagedResourcesAssembler) {
        this.influxBucketsService = influxBucketsService;
        this.assembler = assembler;
        this.bucketPagedResourcesAssembler = bucketPagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity all(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<InfluxBucketWithTagsPojo> result = influxBucketsService.getAllBuckets(pageable);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(bucketPagedResourcesAssembler.toModel(result, assembler));
    }

    @GetMapping("/{name}")
    public BucketRepresentationModel one(@PathVariable String name) {
        InfluxBucketWithTagsPojo result;
        try {
            result = influxBucketsService.getBucketWithTags(name);
        }
        catch (BucketNotFoundException e) {
            logger.info(e.getMessage());
            throw e;
        }

        return assembler.toModel(result);
    }
}
