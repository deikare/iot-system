package com.example.backend.data.controllers.exceptions.advices;

import com.example.backend.data.controllers.exceptions.BucketNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class BucketNotFoundExceptionAdvice {
    @ExceptionHandler({BucketNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String influxNotFoundHandler(BucketNotFoundException e) {
        return e.getMessage();
    }
}
