package com.example.hubservice.utilities.loggers.abstracts;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CrudServiceLogger extends BaseLogger {
    public static void produceCrudServiceLog(Logger logger, @NotNull Object record, CrudOperationType operation) {
        String message = "Operation "  + operation + " on " + record;
        produceLog(logger, message);
    }
}
