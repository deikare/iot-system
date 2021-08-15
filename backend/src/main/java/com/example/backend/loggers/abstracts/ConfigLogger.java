package com.example.backend.loggers.abstracts;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ConfigLogger {

    public static void produceConfigBeanCreationLog(Logger logger, @NotNull Object configBean) {
        String message = "Create configuration Bean: " + configBean;
        produceLog(logger, message);
    }

    public static void produceConfigBeanCreationLog(Logger logger, @NotNull Object configBean, String name) {
        String message = "Create configuration " + name + " Bean: " + configBean;
        produceLog(logger, message);
    }

    private static void produceLog(Logger logger, String message) {
        logger.info(message);
    }
}
