package com.ecommerce.utils;

import com.ecommerce.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            properties.load(fis);
            log.info("Configuration properties loaded successfully from: {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.error("Failed to load configuration properties from: {}", FrameworkConstants.CONFIG_FILE_PATH, e);
            throw new RuntimeException("Could not load config.properties: " + e.getMessage());
        }
    }

    private ConfigReader() {}

    /**
     * Retrieves the configuration value for a key.
     * System properties take precedence over properties file.
     *
     * @param key property key
     * @return property value
     */
    public static String get(String key) {
        String systemVal = System.getProperty(key);
        if (systemVal != null && !systemVal.trim().isEmpty()) {
            return systemVal.trim();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property key '{}' was not found in config.properties", key);
            return "";
        }
        return value.trim();
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        if (val == null || val.isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(val);
    }
}
