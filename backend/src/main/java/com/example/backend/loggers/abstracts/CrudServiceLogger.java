package com.example.backend.loggers.abstracts;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CrudServiceLogger extends BaseLogger {
    public static void produceCrudServiceLog(Logger logger, @NotNull Object record, String operation) {
        String message = "Operation \""  + operation + "\" on " + record;
        produceLog(logger, message);
    }
}
