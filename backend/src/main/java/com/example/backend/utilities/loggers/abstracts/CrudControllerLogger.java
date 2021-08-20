package com.example.backend.utilities.loggers.abstracts;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CrudControllerLogger extends BaseLogger {
    public static void produceCrudControllerLog(Logger logger, HttpMethodType httpMethod, String recordName, Object result) {
        String message = httpMethod + " - " + recordName + " queried, result: " + result;
        produceLog(logger, message);
    }

    public static void produceErrorLog(Logger logger, HttpMethodType httpMethod, String errorMessage) {
        String message = httpMethod + " - " + errorMessage;
        produceLog(logger, message);
    }
}
