package com.example.hubservice.management.hub.exceptions;

public class TooManyHubsExistException extends RuntimeException {
    public TooManyHubsExistException(long actualCount) {
        super("Wrong number of hubs: instead of 1 actual amount = " + actualCount);
    }
}
