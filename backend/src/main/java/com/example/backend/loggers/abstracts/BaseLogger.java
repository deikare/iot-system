package com.example.backend.loggers.abstracts;

import org.slf4j.Logger;

public class BaseLogger {
    static void produceLog(Logger logger, String message) {
        logger.info(message);
    }
}
