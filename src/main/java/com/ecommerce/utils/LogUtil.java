package com.ecommerce.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LogUtil {

    private static final Logger log = LogManager.getLogger(LogUtil.class);

    private LogUtil() {}

    public static void info(String message) {
        log.info(message);
    }

    public static void info(String format, Object... args) {
        log.info(format, args);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void warn(String format, Object... args) {
        log.warn(format, args);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void error(String format, Object... args) {
        log.error(format, args);
    }

    public static void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void debug(String format, Object... args) {
        log.debug(format, args);
    }
}
