package com.example.backend.data.controllers.exceptions;

import java.time.Instant;

public class InfluxRecordNotFoundException extends RuntimeException {
    public InfluxRecordNotFoundException(Instant timestamp) {
        super("Could not find timestamp " + timestamp);
    }
}
