package com.example.backend.loggers.abstracts;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ConfigLogger {

    public static void configBeanCreationLog(Logger logger, @NotNull Object configBean) {
        logger.info("Create configuration Bean: " + configBean);
    }

    public static void configBeanCreationLog(Logger logger, @NotNull Object configBean, String name) {
        logger.info("Create configuration " + name + " Bean: " + configBean);
    }
}
