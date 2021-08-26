package com.example.backend.data.controllers.exceptions;

public class BucketNotFoundException extends RuntimeException {
    public BucketNotFoundException(String bucketName) {
        super("Could not find bucket " + bucketName);
    }
}
