package com.example.hubservice.management.hub.exceptions;

public class WrongNumberOfHubsException extends RuntimeException {
    public WrongNumberOfHubsException(long properCount, long actualCount) {
        super("Wrong number of hubs: instead of " + properCount + " actual amount = " + actualCount);
    }
}
